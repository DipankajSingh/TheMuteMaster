import Image from "next/image";
import Link from "next/link";

export default function Home() {
  return (
    <div className="flex flex-col min-h-screen">
      {/* Header */}
      <header className="px-6 py-4 flex justify-between items-center w-full max-w-7xl mx-auto">
        <div className="flex items-center gap-3">
          <Image src="/images/appicon.png" alt="The Mute Master Logo" width={48} height={48} className="rounded-xl shadow-sm" />
          <h1 className="text-2xl font-bold tracking-tight text-primary">The Mute Master</h1>
        </div>
        <nav>
          <Link href="/privacy" className="text-sm font-medium hover:text-primary transition-colors">
            Privacy Policy
          </Link>
        </nav>
      </header>

      {/* Hero Section */}
      <main className="flex-1 w-full max-w-7xl mx-auto px-6 py-12 md:py-24 flex flex-col md:flex-row items-center gap-12">
        
        {/* Text Content */}
        <div className="flex-1 flex flex-col items-center md:items-start text-center md:text-left space-y-8">
          <h2 className="text-5xl md:text-7xl font-extrabold tracking-tight leading-tight">
            Take Control of Your <br/>
            <span className="text-primary">Device Audio.</span>
          </h2>
          <p className="text-lg md:text-xl max-w-md opacity-80">
            The ultimate tool to manage and mute your Android experience seamlessly. Elegant, lightweight, and built for your convenience.
          </p>
          
          <div className="pt-4 flex flex-col sm:flex-row gap-4">
            <a 
              href="https://play.google.com/store/apps/details?id=com.dipdev.themutemaster&pcampaignid=web_share" 
              target="_blank" 
              rel="noopener noreferrer"
              className="inline-flex items-center justify-center gap-2 bg-primary text-on-primary font-semibold text-lg px-8 py-4 rounded-full shadow-lg hover:bg-primary-container transition-all transform hover:scale-105 active:scale-95"
            >
              Get it on Google Play
            </a>
          </div>
        </div>

        {/* Visual Content (Screenshots) */}
        <div className="flex-1 relative w-full flex justify-center perspective-1000">
          <div className="relative w-64 md:w-80 aspect-[9/19] transform rotate-y-[-15deg] rotate-x-[5deg] shadow-2xl rounded-[2rem] overflow-hidden border-8 border-background bg-foreground/5">
            <Image 
              src="/images/screenshot-1.png" 
              alt="The Mute Master App Screenshot" 
              fill
              className="object-cover"
              priority
            />
          </div>
          {/* Decorative Elements */}
          <div className="absolute -z-10 top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[120%] h-[120%] bg-primary/20 blur-[100px] rounded-full" />
        </div>

      </main>

      {/* Footer */}
      <footer className="w-full py-8 border-t border-foreground/10 text-center opacity-70">
        <p className="text-sm">
          © {new Date().getFullYear()} Dipdev | The Mute Master. All rights reserved.
        </p>
      </footer>
    </div>
  );
}
