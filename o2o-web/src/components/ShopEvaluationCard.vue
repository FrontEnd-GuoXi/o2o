<template>
  <div class="evaluation-card">
    <div class="card-header">
      <div class="shop-name">{{ shopName }}</div>
      <div class="shop-tip">请为本次订单服务打分</div>
    </div>

    <div class="score-list">
      <div class="score-item">
        <span class="score-label">综合评分</span>
        <van-rate
          :model-value="modelValue.totalScore"
          :disabled="submitting"
          void-icon="star"
          void-color="#eee"
          color="#ffb400"
          @update:model-value="value => updateField('totalScore', Number(value))"
        />
      </div>

      <div class="score-item">
        <span class="score-label">服务评分</span>
        <van-rate
          :model-value="modelValue.serviceScore"
          :disabled="submitting"
          void-icon="star"
          void-color="#eee"
          color="#ffb400"
          @update:model-value="value => updateField('serviceScore', Number(value))"
        />
      </div>

      <div class="score-item">
        <span class="score-label">环境评分</span>
        <van-rate
          :model-value="modelValue.environmentScore"
          :disabled="submitting"
          void-icon="star"
          void-color="#eee"
          color="#ffb400"
          @update:model-value="value => updateField('environmentScore', Number(value))"
        />
      </div>
    </div>

    <van-field
      :model-value="modelValue.content"
      type="textarea"
      rows="4"
      autosize
      maxlength="200"
      show-word-limit
      label="评价内容"
      placeholder="说说这次购物体验吧"
      :disabled="submitting"
      @update:model-value="value => updateField('content', String(value))"
    />
  </div>
</template>

<script setup lang="ts">
export interface EvaluationDraft {
  totalScore: number
  serviceScore: number
  environmentScore: number
  content: string
}

const props = defineProps<{
  shopName: string
  modelValue: EvaluationDraft
  submitting?: boolean
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: EvaluationDraft): void
}>()

const updateField = <K extends keyof EvaluationDraft>(key: K, value: EvaluationDraft[K]) => {
  emit('update:modelValue', {
    ...props.modelValue,
    [key]: value
  })
}
</script>

<style scoped>
.evaluation-card {
  background: #fff;
  border-radius: 16px;
  padding: 16px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.05);
}

.card-header {
  margin-bottom: 16px;
}

.shop-name {
  font-size: 16px;
  font-weight: 600;
  color: #323233;
}

.shop-tip {
  margin-top: 6px;
  font-size: 12px;
  color: #969799;
}

.score-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
  margin-bottom: 12px;
}

.score-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.score-label {
  flex-shrink: 0;
  font-size: 14px;
  color: #323233;
}
</style>
