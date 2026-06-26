import type { Metadata } from "next";
import { Inter, Outfit } from "next/font/google";
import "./globals.css";
import { Providers } from "./providers";
import StructuredData from "./components/StructuredData";

const inter = Inter({
  variable: "--font-inter",
  subsets: ["latin"],
});

const outfit = Outfit({
  variable: "--font-outfit",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "The Mute Master - Silent Mode Manager for Android",
  description:
    "The Mute Master is an elegant Android app that automatically silences your phone in quiet zones or on a custom schedule. Manage audio settings with location and time-based automation. Download free on Google Play Store.",
  keywords: [
    "Mute Master",
    "Location-based silent mode",
    "Automatic phone silencer",
    "Auto-mute app",
    "Geofencing silent mode",
    "Smart phone automation",
    "Geofenced Do Not Disturb",
    "Context-aware sound profiles",
    "Location-aware ringer control",
    "Battery-efficient geofencing",
    "auto mute",
    "mute by location",
    "silence by location",
    "time based muting",
    "auto silent mode",
    "Quiet Zones",
    "App to mute phone in meetings automatically",
    "Prevent phone from ringing in class",
    "Automatically turn on Do Not Disturb at location"
  ],
  metadataBase: new URL("https://dipankajsingh.github.io"),
  alternates: {
    canonical: "https://dipankajsingh.github.io/MuteMaster/",
  },
  authors: [{ name: "Dipankaj Singh", url: "https://dipankajsingh.github.io" }],
  creator: "Dipankaj Singh",
  publisher: "Dipdev",
  category: "productivity",
  formatDetection: {
    email: false,
    address: false,
    telephone: false,
  },
  openGraph: {
    title: "The Mute Master - Silent Mode Manager for Android",
    description:
      "Automatically silence your phone in quiet zones or on a schedule. Smart location and time-based muting for Android.",
    url: "https://dipankajsingh.github.io/MuteMaster/",
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
      "Automatically silence your Android phone in quiet zones or on a schedule with smart automation.",
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
        className={`${inter.variable} ${outfit.variable} font-sans antialiased`}
      >
        <link rel="icon" href="/favicon.ico" />
        <StructuredData />
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
