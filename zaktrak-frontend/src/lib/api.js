import axios from "axios";
import {
  getLocalStorage,
  setLocalStorage,
  removeLocalStorage,
} from "./localStorage";

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  (config) => {
    const token = getLocalStorage("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response) {
      if (error.response.status === 401) {
        removeLocalStorage("token");
        removeLocalStorage("user");
        window.location.href = "/login";
      }
      throw new Error(error.response.data.message || "An error occurred");
    } else if (error.request) {
      throw new Error("No response from server");
    } else {
      throw new Error("Error setting up request");
    }
  }
);

export const authAPI = {
  login: async (credentials) => {
    try {
      const response = await api.post("/user/login", credentials);
      if (response.token) {
        setLocalStorage("token", response.token);
      }
      return response;
    } catch (error) {
      throw error;
    }
  },

  register: async (credentials) => {
    try {
      const response = await api.post("/user/register", credentials);
      return response;
    } catch (error) {
      throw error;
    }
  },

  logout: () => {
    removeLocalStorage("token");
    removeLocalStorage("user");
  },

  isAuthenticated: () => {
    return !!getLocalStorage("token");
  },

  getUser: () => {
    return JSON.parse(getLocalStorage("user"));
  },
};

export const userAPI = {
  getProfile: () => api.get("/user/profile"),
  updateProfile: (data) => api.put("/user/profile", data),
};

export const zakatAPI = {
  calculateZakat: () => api.get("/zakat/calculate"),

  recordPayment: (amount, description) =>
    api.post("/zakat-payments", {
      amount,
      description,
    }),

  getAllPayments: () => api.get("/zakat-payments"),

  getRemainingZakat: (totalDue) =>
    api.get(`/zakat-payments/remaining?totalDue=${totalDue}`),

  getTotalPayments: () => api.get("/zakat-payments/total"),

  getAssetsByType: (type) => api.get(`/assets/type/${type}`),

  updateAssetValue: (type, value) =>
    api.put(`/assets/type/${type}/value`, value),

  deleteAsset: (type) => api.delete(`/assets/type/${type}`),
};
export default api;
