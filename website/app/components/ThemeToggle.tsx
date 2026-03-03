"use client";

import { useTheme } from "next-themes";
import { useEffect, useState } from "react";

export function ThemeToggle() {
  const [mounted, setMounted] = useState(false);
  const [isAnimating, setIsAnimating] = useState(false);
  const { theme, setTheme } = useTheme();

  useEffect(() => {
    setMounted(true);
  }, []);

  const handleThemeChange = () => {
    setIsAnimating(true);
    setTheme(theme === "dark" ? "light" : "dark");
    setTimeout(() => setIsAnimating(false), 300);
  };

  if (!mounted) {
    return <div className="w-10 h-10" />;
  }

  return (
    <button
      onClick={handleThemeChange}
      className="relative inline-flex items-center justify-center w-10 h-10 rounded-lg bg-foreground/10 hover:bg-foreground/20 transition-all duration-200 hover:scale-110 active:scale-95"
      aria-label="Toggle theme"
      title={`Switch to ${theme === "dark" ? "light" : "dark"} mode`}
    >
      <div
        className={`transition-all duration-300 ${isAnimating ? "scale-0 rotate-180" : "scale-100 rotate-0"}`}
      >
        {theme === "dark" ? (
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="w-5 h-5 transition-transform duration-300"
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <path d="M12 18C8.68629 18 6 15.3137 6 12C6 8.68629 8.68629 6 12 6C15.3137 6 18 8.68629 18 12C18 15.3137 15.3137 18 12 18ZM12 16C14.2091 16 16 14.2091 16 12C16 9.79086 14.2091 8 12 8C9.79086 8 8 9.79086 8 12C8 14.2091 9.79086 16 12 16ZM11 1H13V4H11V1ZM11 20H13V23H11V20ZM3.51472 4.92893L4.92893 3.51472L7.05025 5.63604L5.63604 7.05025L3.51472 4.92893ZM16.9497 18.364L18.364 16.9497L20.4853 19.0711L19.0711 20.4853L16.9497 18.364ZM19.0711 3.51472L20.4853 4.92893L18.364 7.05025L16.9497 5.63604L19.0711 3.51472ZM5.63604 16.9497L7.05025 18.364L4.92893 20.4853L3.51472 19.0711L5.63604 16.9497ZM23 11V13H20V11H23ZM4 11V13H1V11H4Z" />
          </svg>
        ) : (
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="w-5 h-5 transition-transform duration-300"
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <path d="M10 7C10 10.866 13.134 14 17 14C18.657 14 20.2 13.5259 21.4495 12.75C21.4743 12.4864 21.485 12.2177 21.485 11.945C21.485 7.50594 18.0441 4 14.5 4C11.4917 4 8.98827 5.80685 8.23656 8.34382C8.08619 8.74545 8 9.18797 8 9.645C8 10.3826 8.09182 11.0954 8.26158 11.7684C8.58003 9.95556 9.23768 8.28222 10.2057 6.88274C10.4744 6.46957 10.099 5.90655 9.52631 5.94616C9.30245 5.96085 9.07894 5.97508 8.8563 5.98869C5.62078 6.20859 3 9.01622 3 12.5C3 16.6421 6.35786 20 10.5 20C13.0162 20 15.2914 18.3792 16.2084 16.1437C16.3015 15.9108 16.3915 15.6754 16.4784 15.4375C14.0946 16.2851 11.4397 16.5571 8.73652 15.9828C9.01459 16.5832 9.52731 17.2054 10.2057 17.8627C9.23768 16.4632 8.58003 14.7899 8.26158 12.9761C8.09182 13.639 8 14.3519 8 15.0895C8 15.5455 8.08619 15.9881 8.23656 16.3897C8.98827 18.9259 11.4917 20.7327 14.5 20.7327C18.0441 20.7327 21.485 17.2268 21.485 13.7777C21.485 13.5073 21.4743 13.2386 21.4495 12.975C20.2 15.9999 18.657 16.5027 17 16.5027C13.134 16.5027 10 13.3687 10 9.50274C10 7.98548 10.3634 6.56815 10.99 5.32738C10.4744 6.03043 10 6.9289 10 7Z" />
          </svg>
        )}
      </div>
    </button>
  );
}
