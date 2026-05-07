<template>
  <div class="pay-wait-container">
    <o2o-header title="等待支付" show-back @back="onBack" />

    <div class="pay-content">
      <div class="status-icon">
        <van-icon name="clock-o" size="80" color="#1989fa" />
      </div>

      <h2 class="title">等待支付</h2>
      <p class="desc">请在限定时间内完成支付，否则订单将自动关闭</p>

      <div class="countdown-wrapper">
        <span class="label">剩余支付时间：</span>
        <van-count-down
          :time="time"
          @finish="onTimeout"
          class="custom-countdown"
        >
        </van-count-down>
      </div>

      <div class="order-info" v-if="orderIds.length">
        <div class="info-item">
          <span class="label">订单数量：</span>
          <span class="value">{{ orderIds.length }}</span>
        </div>
        <div class="info-item">
          <span class="label">应付金额：</span>
          <span class="value price">¥{{ totalPrice }}</span>
        </div>
      </div>

      <div class="action-buttons">
        <van-button
          type="primary"
          block
          round
          :disabled="isExpired"
          @click="onPay"
        >
          {{ isExpired ? '订单已关闭' : '立即支付' }}
        </van-button>
        <van-button
          plain
          block
          round
          class="mt-15"
          @click="onBack"
        >
          返回首页
        </van-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { showToast, showLoadingToast, showDialog  } from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import { inventoryDeduction, type PayOrderDTO } from '@/api/order'

const route = useRoute()
const router = useRouter()

// 从路由参数中获取数据
const orderIds = ref<string[]>([])
const token = ref('')
const totalPrice = ref('')
const isExpired = ref(false)

// 倒计时时间，单位毫秒 (30秒)
const time = ref(30 * 1000)

onMounted(() => {
  const idsStr = route.query.orderIds as string
  if (idsStr) {
    orderIds.value = idsStr.split(',')
  }
  token.value = route.query.token as string
  totalPrice.value = route.query.totalPrice as string

  if (!token.value || orderIds.value.length === 0) {
    showToast('订单信息异常')
    router.replace('/home')
  }
})

const onBack = () => {
  router.replace('/home')
}

const onTimeout = () => {
  isExpired.value = true
  showDialog({
    title: '订单已关闭',
    message: '支付超时，订单已自动取消并释放库存',
    theme: 'round-button',
    confirmButtonText: '我知道了'
  }).then(() => {
    router.replace('/home')
  })
}

const onPay = async () => {
  if (isExpired.value) return

  const loading = showLoadingToast({
    message: '支付中...',
    forbidClick: true,
  })

  try {
    const payData: PayOrderDTO = {
      orderList: orderIds.value,
      token: token.value
    }

    const res = await inventoryDeduction(payData)

    loading.close()
    if (res.code === "200") {
      showToast({
        type: 'success',
        message: '支付成功，库存已扣减',
        onClose: () => {
          router.replace('/home')
        }
      })
    } else {
      showToast(res.message || '支付失败')
    }
  } catch (error) {
    loading.close()
    console.error('Pay error:', error)
    showToast('网络异常，请稍后再试')
  }
}
</script>

<style scoped>
.pay-wait-container {
  min-height: 100vh;
  background-color: #f7f8fa;
}

.pay-content {
  padding: 40px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.status-icon {
  margin-bottom: 20px;
}

.title {
  font-size: 20px;
  color: #323233;
  margin-bottom: 10px;
}

.desc {
  font-size: 14px;
  color: #969799;
  margin-bottom: 30px;
}

.countdown-wrapper {
  display: flex;
  align-items: center;
  font-size: 18px;
  color: #323233;
  margin-bottom: 40px;
  background: #fff;
  padding: 15px 25px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.countdown-wrapper .label {
  margin-right: 5px;
}

.custom-countdown {
  color: #ee0a24;
  font-weight: bold;
  font-size: 20px;
}

.time-item {
  display: inline-block;
  width: 30px;
  text-align: center;
}

.time-unit {
  font-size: 14px;
  margin-left: 2px;
  color: #323233;
}

.order-info {
  width: 100%;
  background: #fff;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 40px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 14px;
}

.info-item:last-child {
  margin-bottom: 0;
}

.info-item .label {
  color: #646566;
}

.info-item .value {
  color: #323233;
  font-weight: 500;
}

.info-item .value.price {
  color: #ee0a24;
  font-size: 16px;
}

.action-buttons {
  width: 100%;
}

.mt-15 {
  margin-top: 15px;
}
</style>
