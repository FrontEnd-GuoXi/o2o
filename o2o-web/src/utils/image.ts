// 默认图片URL
const DEFAULT_IMAGE_URL = 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c'

/**
 * 获取完整的图片URL
 * @param path 后端返回的图片路径
 * @returns 完整的图片URL
 */
export const getImageUrl = (path: string) => {
  if (!path) return DEFAULT_IMAGE_URL
  // 如果已经是完整路径则直接返回
  if (path.startsWith('http')) return path
  // 否则加上 /api 前缀，以便通过 vite 代理访问后端资源
  return `/api${path.startsWith('/') ? '' : '/'}${path}`
}



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
