import AuthForm from "@/components/forms/AuthForm";

export default function LoginPage() {
  return (
    <div className="min-h-screen flex items-center justify-center px-4 py-12 bg-slate-50">
      <div className="w-full max-w-md">
        <AuthForm type="login" />
      </div>
    </div>
  );
}
