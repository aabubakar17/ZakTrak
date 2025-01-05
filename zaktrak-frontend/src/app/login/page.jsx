import AuthForm from "@/components/forms/AuthForm";

export default function LoginPage() {
  return (
    <div className="min-h-screen flex flex-grow items-center justify-center py-12 bg-slate-50">
      <div className="w-full max-w-lg">
        <AuthForm type="login" />
      </div>
    </div>
  );
}
