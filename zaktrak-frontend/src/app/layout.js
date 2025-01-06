import { Inter } from "next/font/google";
import "./globals.css";
import Navbar from "@/components/layout/Navbar";
import Footer from "@/components/layout/Footer";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "ZakTrak",
  description: "Track and manage your Zakat obligations with ease",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className={inter.className} suppressHydrationWarning>
        <Navbar />
        <main className="min-h-screen bg-slate-50">{children}</main>
        <Footer />
      </body>
    </html>
  );
}
