"use client";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import Link from "next/link";

const AuthForm = ({ type = "login" }) => {
  // Form state
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    name: "",
  });

  // Email validation in real-time
  const [emailError, setEmailError] = useState("");
  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!email) {
      setEmailError("Email is required");
    } else if (!emailRegex.test(email)) {
      setEmailError("Please enter a valid email address");
    } else {
      setEmailError("");
    }
  };

  // Password validation in real-time
  const [passwordError, setPasswordError] = useState("");
  const validatePassword = (password) => {
    if (!password) {
      setPasswordError("Password is required");
    } else if (password.length < 8) {
      setPasswordError("Password must be at least 8 characters long");
    } else {
      setPasswordError("");
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    if (name === "email") validateEmail(value);
    if (name === "password") validatePassword(value);
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    validateEmail(formData.email);
    validatePassword(formData.password);

    if (!emailError && !passwordError) {
      // Here you would typically make an API call to your backend
      console.log("Form submitted:", formData);
      // After successful authentication, redirect to dashboard
      // window.location.href = '/dashboard'
    }
  };

  return (
    <Card className="w-full max-w-md mx-auto">
      <CardHeader>
        <CardTitle>
          {type === "login" ? "Welcome Back" : "Create Account"}
        </CardTitle>
        <CardDescription>
          {type === "login"
            ? "Enter your credentials to access your account"
            : "Sign up to start tracking your zakat"}
        </CardDescription>
      </CardHeader>
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-4">
          {/* Name field - only shown for registration */}
          {type === "register" && (
            <div className="space-y-2">
              <Label htmlFor="name">Name (Optional)</Label>
              <Input
                id="name"
                name="name"
                type="text"
                placeholder="Your name"
                value={formData.name}
                onChange={handleChange}
              />
            </div>
          )}

          {/* Email field */}
          <div className="space-y-2">
            <Label htmlFor="email">Email</Label>
            <Input
              id="email"
              name="email"
              type="email"
              placeholder="you@example.com"
              value={formData.email}
              onChange={handleChange}
              className={emailError ? "border-red-500" : ""}
            />
            {emailError && <p className="text-sm text-red-500">{emailError}</p>}
          </div>

          {/* Password field */}
          <div className="space-y-2">
            <Label htmlFor="password">Password</Label>
            <Input
              id="password"
              name="password"
              type="password"
              placeholder="Enter your password"
              value={formData.password}
              onChange={handleChange}
              className={passwordError ? "border-red-500" : ""}
            />
            {passwordError && (
              <p className="text-sm text-red-500">{passwordError}</p>
            )}
          </div>
        </CardContent>

        <CardFooter className="flex flex-col space-y-4">
          <Button
            type="submit"
            className="w-full bg-emerald-600 hover:bg-emerald-700"
          >
            {type === "login" ? "Sign In" : "Create Account"}
          </Button>

          <p className="text-sm text-slate-600 text-center">
            {type === "login" ? (
              <>
                Don't have an account?{" "}
                <Link
                  href="/register"
                  className="text-emerald-600 hover:text-emerald-700"
                >
                  Sign up
                </Link>
              </>
            ) : (
              <>
                Already have an account?{" "}
                <Link
                  href="/login"
                  className="text-emerald-600 hover:text-emerald-700"
                >
                  Sign in
                </Link>
              </>
            )}
          </p>
        </CardFooter>
      </form>
    </Card>
  );
};

export default AuthForm;
