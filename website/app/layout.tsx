import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { Providers } from "./providers";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "The Mute Master - Take Control of Your Audio",
  description:
    "The Mute Master is an elegant and lightweight utility to manage and mute your Android experience seamlessly. Get it on the Google Play Store.",
  keywords: [
    "Mute Master",
    "Android App",
    "Audio Control",
    "Volume Manager",
    "Mute App",
  ],
  openGraph: {
    title: "The Mute Master - Take Control of Your Audio",
    description:
      "The ultimate tool to manage and mute your Android experience seamlessly.",
    url: "https://mutemaster.com",
    siteName: "The Mute Master",
    images: [
      {
        url: "/images/appicon.png",
        width: 512,
        height: 512,
        alt: "The Mute Master Icon",
      },
      {
        url: "/images/screenshot-1.png",
        width: 1080,
        height: 1920,
        alt: "The Mute Master Screenshot 1",
      },
    ],
    locale: "en_US",
    type: "website",
  },
  twitter: {
    card: "summary_large_image",
    title: "The Mute Master",
    description:
      "Manage your Android audio with ease. Elegant and lightweight.",
    images: ["/images/appicon.png"],
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        <link rel="icon" href="/favicon.ico" />
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
