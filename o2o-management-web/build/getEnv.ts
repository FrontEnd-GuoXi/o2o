import path from "path";

/**
 * 是否为开发环境
 * @param mode
 */
export function isDevFn(mode: string): boolean {
  return mode === "development";
}

/**
 * 是否为生产环境
 * @param mode
 */
export function isProdFn(mode: string): boolean {
  return mode === "production";
}

/**
 * 是否为测试环境
 * @param mode
 */
export function isTestFn(mode: string): boolean {
  return mode === "test";
}

/**
 * 是否生成打包分析报告
 */
export function isReportMode(): boolean {
  return process.env.VITE_REPORT === "true";
}

/**
 * 读取所有环境变量配置文件，并对其做类型转换
 * @param envConf
 */
export function wrapperEnv(envConf: Recordable): ViteEnv {
  const ret: any = {};
  for (const envName of Object.keys(envConf)) {
    let realName = envConf[envName].replace(/\\n/g, "\n");
    realName = realName === "true" ? true : realName === "false" ? false : realName;
    if (envName === "VITE_PORT") realName = Number(realName);
    if (envName === "VITE_PROXY") {
      try {
        realName = JSON.parse(realName);
      } catch (error) {
        console.error(error);
      }
    }
    ret[envName] = realName;
  }
  return ret;
}

/**
 * 获取用户根目录
 * @param dir 文件路径
 */
export function getRootPath(...dir: string[]) {
  return path.resolve(process.cwd(), ...dir);
}
