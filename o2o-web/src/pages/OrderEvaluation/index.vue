<template>
  <div class="order-evaluation-container">
    <O2oHeader title="订单评价" />

    <div class="page-content">
      <div class="page-summary">
        <div class="summary-title">支付成功</div>
        <div class="summary-desc">请完成本次订单评价，帮助其他用户了解店铺服务。</div>
        <div v-if="totalPrice" class="summary-price">实付金额：¥{{ totalPrice }}</div>
      </div>

      <div class="card-list">
        <ShopEvaluationCard
          v-for="(item, index) in evaluationForms"
          :key="`${item.shopId}-${item.orderId}`"
          :shop-name="item.shopName"
          :model-value="item"
          :submitting="submitting"
          @update:model-value="value => updateEvaluation(index, value)"
        />
      </div>

      <div class="action-bar">
        <van-button
          type="primary"
          block
          round
          :loading="submitting"
          @click="submitEvaluations"
        >
          提交评价
        </van-button>
        <van-button
          plain
          block
          round
          class="skip-button"
          :disabled="submitting"
          @click="goHome"
        >
          暂不评价
        </van-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { showLoadingToast, showToast } from 'vant'
import O2oHeader from '@/components/O2oHeader.vue'
import ShopEvaluationCard, { type EvaluationDraft } from '@/components/ShopEvaluationCard.vue'
import { addEvaluation } from '@/api/evaluation'
import { useUserStore } from '@/stores/user'
import { deserializeEvaluationOrders } from '@/utils/orderEvaluation'

interface EvaluationForm extends EvaluationDraft {
  shopId: number | string
  orderId: number | string
  shopName: string
}

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const totalPrice = ref('')
const submitting = ref(false)
const evaluationForms = ref<EvaluationForm[]>([])

const goHome = () => {
  router.replace('/home')
}

const initEvaluationForms = async () => {
  totalPrice.value = (route.query.totalPrice as string) || ''

  const evaluationOrders = deserializeEvaluationOrders(route.query.evaluationOrders as string | undefined)
  if (!evaluationOrders.length) {
    showToast('评价订单信息缺失')
    goHome()
    return
  }

  if (!userStore.userInfo) {
    await userStore.fetchUserInfo()
  }

  if (!userStore.userInfo?.userId) {
    showToast('用户信息失效，请重新登录')
    goHome()
    return
  }

  evaluationForms.value = evaluationOrders.map(item => ({
    shopId: item.shopId,
    orderId: item.orderId,
    shopName: item.shopName,
    totalScore: 5,
    serviceScore: 5,
    environmentScore: 5,
    content: ''
  }))
}

const updateEvaluation = (index: number, value: EvaluationDraft) => {
  const current = evaluationForms.value[index]
  if (!current) {
    return
  }

  evaluationForms.value[index] = {
    ...current,
    ...value
  }
}

const validateForms = () => {
  const invalidItem = evaluationForms.value.find(item => !item.content.trim())
  if (invalidItem) {
    showToast(`请填写${invalidItem.shopName}的评价内容`)
    return false
  }

  return true
}

const submitEvaluations = async () => {
  if (submitting.value || !evaluationForms.value.length) {
    return
  }

  if (!validateForms()) {
    return
  }

  const userId = Number(userStore.userInfo?.userId)
  if (!userId) {
    showToast('用户信息失效，请重新登录')
    return
  }

  submitting.value = true
  const loading = showLoadingToast({
    message: '提交评价中...',
    forbidClick: true
  })

  try {
    for (const item of evaluationForms.value) {
      const res = await addEvaluation({
        shopId: Number(item.shopId),
        userId,
        orderId: Number(item.orderId),
        totalScore: Math.round(item.totalScore),
        serviceScore: Math.round(item.serviceScore),
        environmentScore: Math.round(item.environmentScore),
        content: item.content.trim()
      })

      if (res.code !== '200') {
        throw new Error(res.message || `${item.shopName}评价提交失败`)
      }
    }

    loading.close()
    showToast({
      type: 'success',
      message: '评价提交成功',
      onClose: goHome
    })
  } catch (error) {
    loading.close()
    console.error('Submit evaluation error:', error)
    showToast(error instanceof Error ? error.message : '评价提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  initEvaluationForms()
})
</script>

<style scoped>
.order-evaluation-container {
  min-height: 100vh;
  background: #f7f8fa;
}

.page-content {
  padding: 16px;
}

.page-summary {
  background: linear-gradient(135deg, #e8f3ff 0%, #f4f9ff 100%);
  border-radius: 16px;
  padding: 18px 16px;
  margin-bottom: 16px;
}

.summary-title {
  font-size: 18px;
  font-weight: 600;
  color: #1989fa;
}

.summary-desc {
  margin-top: 8px;
  font-size: 14px;
  line-height: 1.5;
  color: #4f5660;
}

.summary-price {
  margin-top: 10px;
  font-size: 15px;
  font-weight: 600;
  color: #323233;
}

.card-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.action-bar {
  margin-top: 24px;
  padding-bottom: 24px;
}

.skip-button {
  margin-top: 12px;
}
</style>
