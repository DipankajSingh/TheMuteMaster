"use client";

import { ThemeProvider } from "next-themes";
import { ReactNode } from "react";

export function Providers({ children }: { children: ReactNode }) {
  return (
    <ThemeProvider
      attribute="class"
      defaultTheme="system"
      enableSystem
      storageKey="theme-preference"
      forcedTheme={undefined}
      enableColorScheme={false}
      disableTransitionOnChange={false}
    >
      {children}
    </ThemeProvider>
  );
}
