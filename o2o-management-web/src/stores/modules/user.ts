import { defineStore } from "pinia";
import { ref } from "vue";

import type { UserInfo, UserState } from "@/stores/interface";

export const useUserStore = defineStore(
  "geeker-user",
  () => {
    const token = ref<string>("");
    const setToken = (newToken: string) => {
      token.value = newToken;
    };

    const userInfo = ref<UserInfo>({
      userId: "",
      name: "",
      gender: "",
      userType: 0,
      profileImg: ""
    });
    const setUserInfo = (newUserInfo: UserInfo) => {
      userInfo.value = newUserInfo;
    };

    return {
      token,
      userInfo,
      setToken,
      setUserInfo
    };
  },
  {
    persist: {
      key: "geeker-user",
      storage: localStorage
    }
  }
);
