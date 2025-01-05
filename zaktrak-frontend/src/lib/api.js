import axios from "axios";

// Create axios instance with default config
const api = axios.create({
  baseURL: "http://79.72.76.191:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response) {
      // Server responded with error status
      if (error.response.status === 401) {
        localStorage.removeItem("token");
        // You might want to redirect to login page here
      }
      throw new Error(error.response.data.message || "An error occurred");
    } else if (error.request) {
      // Request made but no response received
      throw new Error("No response from server");
    } else {
      // Something happened in setting up the request
      throw new Error("Error setting up request");
    }
  }
);

// Auth functions
export const authAPI = {
  login: async (credentials) => {
    try {
      const response = await api.post("/user/login", credentials);
      if (response.token) {
        localStorage.setItem("token", response.token);
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
    localStorage.removeItem("token");
  },

  isAuthenticated: () => {
    return !!localStorage.getItem("token");
  },

  getUser: () => {
    return JSON.parse(localStorage.getItem("user"));
  },
};

// Example of how to structure other API calls
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
