import pxToViewport from 'postcss-px-to-viewport-8-plugin'

export default {
  plugins: [
    pxToViewport({
      viewportWidth: 375,  // 你的设计稿宽度
      unitPrecision: 3,
      viewportUnit: 'vw',
      selectorBlackList: [],
      minPixelValue: 1,
      mediaQuery: false
    })
  ]
}
