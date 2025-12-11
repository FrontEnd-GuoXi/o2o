// HTTP请求工具，封装fetch请求并添加拦截器功能

// 请求配置接口
export interface RequestConfig {
  url: string;
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
  data?: any;
  headers?: Record<string, string>;
  // 其他fetch选项
  [key: string]: any;
}

// 响应数据接口
export interface ResponseData<T = any> {
  code: string;
  message: string;
  data: T;
  [key: string]: any;
}

// 创建请求实例
const request = async <T = any>(config: RequestConfig): Promise<ResponseData<T>> => {
  const {
    url,
    method = 'GET',
    data,
    headers = {},
    ...otherOptions
  } = config;

  try {
    // 请求拦截器：添加通用请求头
    const requestHeaders: Record<string, string> = {
      'Content-Type': 'application/json',
      ...headers
    };

    // 为除login和register之外的接口添加Authorization请求头
    const isAuthApi = url.includes('/auth/login') || url.includes('/auth/register');
    if (!isAuthApi) {
      const token = localStorage.getItem('token');
      if (token) {
        requestHeaders['Authorization'] = `Bearer ${token}`;
      }
    }

    // 构建请求选项
    const requestOptions: RequestInit = {
      method,
      headers: requestHeaders,
      ...otherOptions
    };

    // 如果有请求体，添加到请求中
    if (data && (method === 'POST' || method === 'PUT' || method === 'PATCH')) {
      requestOptions.body = JSON.stringify(data);
    }

    // 发送请求
    const response = await fetch(url, requestOptions);

    // 解析响应数据
    let responseData: any;
    try {
      responseData = await response.json();
    } catch (error) {
      throw new Error(`响应数据解析失败: ${error instanceof Error ? error.message : '未知错误'}`);
    }

    // 响应拦截器：统一处理错误
    if (!response.ok) {
      throw new Error(responseData.message || `请求失败：${response.status}`);
    }

    // 业务逻辑判断：根据code判断请求是否真正成功
    if (responseData.code !== '200') {
      // 导入Vant Toast组件（动态导入，避免SSR问题）
      const { showToast } = await import('vant');
      // 显示错误提示
      showToast({
        message: responseData.message || '请求失败',
        position: 'top',
        duration: 2000
      });
      // 抛出错误，让调用方可以处理
      throw new Error(responseData.message || '请求失败');
    }



    // 可以在这里添加全局响应处理逻辑
    // 例如：如果token过期，跳转到登录页

    return responseData;
  } catch (error) {
    // 统一错误处理
    console.error('请求错误:', error);

    // 重新抛出错误，让调用方可以处理
    throw error instanceof Error ? error : new Error('网络请求失败');
  }
};

// 导出常用的请求方法
export default {
  get: <T = any>(url: string, config?: Omit<RequestConfig, 'url' | 'method'>) => {
    return request<T>({ url, method: 'GET', ...config });
  },
  post: <T = any>(url: string, data?: any, config?: Omit<RequestConfig, 'url' | 'method' | 'data'>) => {
    return request<T>({ url, method: 'POST', data, ...config });
  },
  put: <T = any>(url: string, data?: any, config?: Omit<RequestConfig, 'url' | 'method' | 'data'>) => {
    return request<T>({ url, method: 'PUT', data, ...config });
  },
  delete: <T = any>(url: string, config?: Omit<RequestConfig, 'url' | 'method'>) => {
    return request<T>({ url, method: 'DELETE', ...config });
  },
  patch: <T = any>(url: string, data?: any, config?: Omit<RequestConfig, 'url' | 'method' | 'data'>) => {
    return request<T>({ url, method: 'PATCH', data, ...config });
  }
};
