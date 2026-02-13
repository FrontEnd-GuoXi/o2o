/**
 * 格式化日期时间
 * @param dateStr - 日期字符串或毫秒数
 * @returns 格式化后的日期字符串 (YYYY-MM-DD HH:mm:ss)
 */
export const formatDate = (dateStr: string | number) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')

  return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`
}
