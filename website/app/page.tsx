import Image from "next/image";
import Link from "next/link";

export default function Home() {
  return (
    <div className="flex flex-col min-h-screen">
      {/* Header */}
      <header className="px-6 py-4 flex justify-between items-center w-full max-w-7xl mx-auto z-10 sticky top-0 bg-background/80 backdrop-blur-md border-b border-foreground/5">
        <div className="flex items-center gap-3">
          <Image src="/images/appicon.png" alt="The Mute Master Logo" width={40} height={40} className="rounded-xl shadow-sm" />
          <h1 className="text-xl font-bold tracking-tight text-primary">The Mute Master</h1>
        </div>
        <nav>
          <Link href="/privacy" className="text-sm font-medium hover:text-primary transition-colors">
            Privacy Policy
          </Link>
        </nav>
      </header>

      {/* Hero Section */}
      <section className="relative w-full max-w-7xl mx-auto px-6 py-20 md:py-32 flex flex-col md:flex-row items-center gap-12 overflow-hidden">
        {/* Text Content */}
        <div className="flex-1 flex flex-col items-center md:items-start text-center md:text-left space-y-8 z-10">
          <h2 className="text-5xl md:text-7xl font-extrabold tracking-tight leading-tight">
            Take Control of Your <br />
            <span className="text-primary bg-clip-text">Device Audio.</span>
          </h2>
          <p className="text-lg md:text-2xl max-w-lg opacity-80 leading-relaxed">
            The ultimate tool to manage and mute your Android experience seamlessly. Set regions, forget the rest.
          </p>

          <div className="pt-4 flex flex-col sm:flex-row gap-4 w-full justify-center md:justify-start">
            <a
              href="https://play.google.com/store/apps/details?id=com.dipdev.themutemaster&pcampaignid=web_share"
              target="_blank"
              rel="noopener noreferrer"
              className="inline-flex items-center justify-center gap-2 bg-primary text-on-primary font-semibold text-lg px-8 py-4 rounded-full shadow-[0_0_40px_-10px_rgba(180,29,0,0.6)] hover:shadow-[0_0_60px_-10px_rgba(180,29,0,0.8)] transition-all transform hover:-translate-y-1 active:scale-95"
            >
              Get it on Google Play
            </a>
          </div>

          <div className="flex items-center gap-3 mt-4 opacity-70">
            <div className="flex -space-x-2">
              {[1, 2, 3, 4, 5].map((i) => (
                <div key={i} className="w-8 h-8 rounded-full bg-foreground/20 border-2 border-background flex items-center justify-center text-xs">
                  {['🌟', '🔥', '🚀', '✨', '🎉'][i - 1]}
                </div>
              ))}
            </div>
            <p className="text-sm font-medium">Loved by Android users worldwide</p>
          </div>
        </div>

        {/* Visual Content (Screenshots) */}
        <div className="flex-1 relative w-full flex justify-center perspective-1000 z-10 mt-10 md:mt-0">
          <div className="relative w-64 md:w-[320px] aspect-[9/19] transform md:-rotate-y-12 md:rotate-x-12 shadow-2xl rounded-[2.5rem] overflow-hidden border-[10px] border-foreground/5 bg-background transition-transform hover:rotate-0 duration-700 ease-in-out">
            <Image
              src="/images/screenshot-1.png"
              alt="The Mute Master App Screenshot"
              fill
              className="object-cover"
              priority
            />
          </div>
          <div className="absolute -z-10 top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full h-[120%] bg-primary/20 blur-[100px] rounded-full" />
        </div>
      </section>

      {/* Features Grid */}
      <section className="bg-foreground/5 py-24 w-full">
        <div className="max-w-7xl mx-auto px-6">
          <div className="text-center mb-16 space-y-4">
            <h2 className="text-4xl font-bold tracking-tight">Everything You Need, Nothing You Don't.</h2>
            <p className="text-lg opacity-70 max-w-2xl mx-auto">Built purely to solve the frustration of forgetting to silence your phone.</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Feature 1 */}
            <div className="bg-background p-8 rounded-3xl shadow-sm border border-foreground/10 hover:shadow-xl hover:border-primary/30 transition-all duration-300 transform hover:-translate-y-1">
              <div className="w-14 h-14 bg-primary/10 text-primary flex items-center justify-center rounded-2xl mb-6 text-2xl">
                📍
              </div>
              <h3 className="text-xl font-bold mb-3">Smart Geofencing</h3>
              <p className="opacity-70 leading-relaxed">
                Set digital perimeters around your office, library, or church. Your phone will auto-mute the moment you walk in.
              </p>
            </div>

            {/* Feature 2 */}
            <div className="bg-background p-8 rounded-3xl shadow-sm border border-foreground/10 hover:shadow-xl hover:border-primary/30 transition-all duration-300 transform hover:-translate-y-1">
              <div className="w-14 h-14 bg-primary/10 text-primary flex items-center justify-center rounded-2xl mb-6 text-2xl">
                🔋
              </div>
              <h3 className="text-xl font-bold mb-3">Battery Efficient</h3>
              <p className="opacity-70 leading-relaxed">
                We utilize Android's native deep geo-trigger APIs to ensure your battery life remains mostly untouched throughout the day.
              </p>
            </div>

            {/* Feature 3 */}
            <div className="bg-background p-8 rounded-3xl shadow-sm border border-foreground/10 hover:shadow-xl hover:border-primary/30 transition-all duration-300 transform hover:-translate-y-1">
              <div className="w-14 h-14 bg-primary/10 text-primary flex items-center justify-center rounded-2xl mb-6 text-2xl">
                🛡️
              </div>
              <h3 className="text-xl font-bold mb-3">Privacy First</h3>
              <p className="opacity-70 leading-relaxed">
                Your location data is 100% local. It never leaves your phone and there are no servers tracking your coordinates.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* How it Works / Screenshots */}
      <section className="py-24 max-w-7xl mx-auto px-6 w-full">
        <div className="flex flex-col md:flex-row items-center gap-16">
          <div className="flex-1 space-y-8">
            <h2 className="text-4xl font-bold tracking-tight">Set it up in under a minute.</h2>
            <div className="space-y-6">
              <div className="flex gap-4 items-start">
                <div className="flex-shrink-0 w-8 h-8 rounded-full bg-primary text-on-primary flex items-center justify-center font-bold">1</div>
                <div>
                  <h4 className="text-xl font-bold mb-1">Open the Map</h4>
                  <p className="opacity-70">Search for your desired quiet zones.</p>
                </div>
              </div>
              <div className="flex gap-4 items-start">
                <div className="flex-shrink-0 w-8 h-8 rounded-full bg-primary text-on-primary flex items-center justify-center font-bold">2</div>
                <div>
                  <h4 className="text-xl font-bold mb-1">Drop a Pin</h4>
                  <p className="opacity-70">Define the radius of the auto-mute zone.</p>
                </div>
              </div>
              <div className="flex gap-4 items-start">
                <div className="flex-shrink-0 w-8 h-8 rounded-full bg-primary text-on-primary flex items-center justify-center font-bold">3</div>
                <div>
                  <h4 className="text-xl font-bold mb-1">Forget About It</h4>
                  <p className="opacity-70">Let MuteMaster automatically handle your Do Not Disturb settings based on your location.</p>
                </div>
              </div>
            </div>
          </div>
          <div className="flex-1 flex justify-center relative">
            <div className="relative w-64 md:w-[300px] aspect-[9/19] shadow-2xl rounded-[2.5rem] overflow-hidden border-[8px] border-foreground/10 bg-background">
              <Image
                src="/images/screenshot-4.png"
                alt="Adding a new location"
                fill
                className="object-cover"
              />
            </div>
          </div>
        </div>
      </section>

      {/* Final CTA */}
      <section className="bg-primary/5 py-24 border-y border-primary/10 mt-auto">
        <div className="max-w-4xl mx-auto px-6 text-center space-y-8">
          <h2 className="text-4xl md:text-5xl font-extrabold tracking-tight">Ready to silence the distractions?</h2>
          <p className="text-xl opacity-80 pb-4">Download The Mute Master for free today and never manually mute your phone at work again.</p>
          <a
            href="https://play.google.com/store/apps/details?id=com.dipdev.themutemaster&pcampaignid=web_share"
            target="_blank"
            rel="noopener noreferrer"
            className="inline-flex items-center justify-center bg-foreground text-background font-semibold text-xl px-10 py-5 rounded-full shadow-lg hover:scale-105 transition-transform"
          >
            Download on Google Play Now
          </a>
        </div>
      </section>

      {/* Footer */}
      <footer className="w-full py-12 text-center opacity-60 flex flex-col items-center gap-4">
        <div className="flex gap-4 mb-2">
          <Link href="/privacy" className="hover:text-primary transition-colors">Privacy Policy</Link>
          <span>•</span>
          <a href="mailto:support@mutemasterapp.com" className="hover:text-primary transition-colors">Support</a>
        </div>
        <p className="text-sm">
          © {new Date().getFullYear()} Dipdev | The Mute Master. All rights reserved.
        </p>
      </footer>
    </div>
  );
}
