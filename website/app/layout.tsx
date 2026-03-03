import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { Providers } from "./providers";
import StructuredData from "./components/StructuredData";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "The Mute Master - Silent Mode Manager for Android",
  description:
    "The Mute Master is an elegant and lightweight Android app that automatically silences your phone in quiet zones. Manage audio settings with geolocation-based automation. Download free on Google Play Store.",
  keywords: [
    "Mute Master",
    "Android App",
    "Audio Control",
    "Volume Manager",
    "Mute App",
    "Silent Mode",
    "Geofencing",
    "Quiet Zones",
    "Do Not Disturb",
  ],
  metadataBase: new URL("https://mutemaster.com"),
  alternates: {
    canonical: "https://mutemaster.com",
  },
  openGraph: {
    title: "The Mute Master - Silent Mode Manager for Android",
    description:
      "Automatically silence your phone in quiet zones. Smart geolocation-based muting for Android.",
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
        alt: "The Mute Master Screenshot",
      },
    ],
    locale: "en_US",
    type: "website",
  },
  twitter: {
    card: "summary_large_image",
    title: "The Mute Master",
    description:
      "Automatically silence your Android phone in quiet zones with smart geolocation.",
    images: ["/images/appicon.png"],
    creator: "@dipdev",
  },
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      "max-snippet": -1,
      "max-image-preview": "large",
      "max-video-preview": -1,
    },
  },
  applicationName: "The Mute Master",
  appleWebApp: {
    capable: true,
    statusBarStyle: "black-translucent",
    title: "The Mute Master",
  },
  verification: {
    google: "I0s8adF00P2S6g0j_zO8U0UX2xT0cOZAKPwwJyR5bVo",
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
        <StructuredData />
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
