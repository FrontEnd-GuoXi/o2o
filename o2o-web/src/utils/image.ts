// 默认图片URL
const DEFAULT_IMAGE_URL = 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c'

/**
 * 处理图片加载错误
 * @param event 错误事件对象
 * @param customDefaultUrl 自定义默认图片URL（可选）
 */
export const handleImageError = (event: Event, customDefaultUrl?: string) => {
  const target = event.target as HTMLImageElement
  target.onerror = null
  target.src = customDefaultUrl || DEFAULT_IMAGE_URL
}
