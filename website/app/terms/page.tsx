import Link from "next/link";
export const metadata = { title: "Terms & Conditions | The Mute Master" };

export default function TermsAndConditions() {
  return (
    <div className="min-h-screen py-10 px-6 max-w-4xl mx-auto">
      <Link
        href="/"
        className="text-primary hover:text-primary-container transition-colors font-medium mb-8 inline-block"
      >
        &larr; Back to Home
      </Link>

      <div className="prose prose-stone dark:prose-invert max-w-none">
        <h1 className="text-3xl font-bold uppercase border-b-2 border-foreground pb-2 mb-6">
          Terms & Conditions
        </h1>

        <p className="mb-2">
          <strong>Application Name:</strong> MuteMaster - Auto Silent Manager
        </p>
        <p className="mb-6">
          <strong>Effective Date:</strong> January 23, 2026
        </p>

        <p className="mb-8">
          By downloading or using the app, these terms will automatically apply
          to you. You should make sure therefore that you read them carefully
          before using the app. You are not allowed to copy or modify the app,
          any part of the app, or our trademarks in any way.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          1. Use of Background Location
        </h2>
        <p className="mb-4">
          The MuteMaster application requires background location access (geofencing) 
          to function correctly. This enables the &apos;Automatic Mute&apos; feature, silencing 
          your device when you enter specific zones you designate, even when the app 
          is closed. By using this application, you consent to this local processing 
          of background location data. This location data is stored strictly locally 
          on your device and is not transmitted to our servers or third parties.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          2. Third-Party Services
        </h2>
        <p className="mb-4">
          The app does use third-party services that declare their own Terms and Conditions.
          In order to improve app stability and understand user interactions, we integrate
          Firebase Crashlytics and Google Analytics for Firebase. These services may collect
          anonymized usage data and crash reports. They do not access your geofencing locations.
        </p>
        <p className="mb-4">
          Links to Terms and Conditions of third-party service providers used by the app:
        </p>
        <ul className="list-disc pl-6 mb-8 space-y-2">
          <li>
            <a
              href="https://policies.google.com/terms"
              className="text-primary hover:underline"
              target="_blank"
              rel="noopener noreferrer"
            >
              Google Play Services
            </a>
          </li>
          <li>
            <a
              href="https://firebase.google.com/terms/"
              className="text-primary hover:underline"
              target="_blank"
              rel="noopener noreferrer"
            >
              Google Analytics for Firebase
            </a>
          </li>
          <li>
            <a
              href="https://firebase.google.com/terms/crashlytics/"
              className="text-primary hover:underline"
              target="_blank"
              rel="noopener noreferrer"
            >
              Firebase Crashlytics
            </a>
          </li>
        </ul>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          3. App Usage and Limitations
        </h2>
        <p className="mb-4">
          We are committed to ensuring that the app is as useful and efficient
          as possible. For that reason, we reserve the right to make changes to
          the app or to charge for its services, at any time and for any reason.
        </p>
        <p className="mb-4">
          You should be aware that there are certain things that we will not
          take responsibility for. Certain functions of the app will require the
          app to have an active internet connection (for maps to load, analytics, and crash reporting). 
          The connection can be Wi-Fi or provided by your mobile network provider.
        </p>
        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          4. No Warranty (&quot;As Is&quot; Clause)
        </h2>
        <p className="mb-4">
          The Application is provided on an &quot;AS IS&quot; and &quot;AS AVAILABLE&quot; basis. The Developer (Dipankaj) makes no warranties that the Application will be error-free or that the geofencing and time-scheduling features will work perfectly on every device or in every location.
        </p>
        <p className="mb-4">
          <strong>You acknowledge that you are solely responsible</strong> for ensuring your device is silenced in sensitive environments. We are not liable for any missed calls, failed mute events, or any social or professional consequences resulting from the app&apos;s performance.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          5. Limitation of Liability
        </h2>
        <p className="mb-4">
          To the maximum extent permitted by applicable law, in no event shall the Developer be liable for any direct, indirect, incidental, or consequential damages arising out of your use or inability to use the Application.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          6. Governing Law
        </h2>
        <p className="mb-4">
          These Terms shall be governed by the laws of <strong>New Delhi, India</strong>. Any disputes arising from this App shall be subject to the exclusive jurisdiction of the courts in New Delhi.
        </p>
        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          7. Changes to This Terms and Conditions
        </h2>
        <p className="mb-8">
          We may update our Terms and Conditions from time to time. Thus, you
          are advised to review this page periodically for any changes. We will
          notify you of any changes by posting the new Terms and Conditions on
          this page.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          8. Contact Us
        </h2>
        <p className="mb-8">
          If you have any questions or suggestions about our Terms and
          Conditions, do not hesitate to contact us at dipankajsingh25@gmail.com.
        </p>
      </div>
    </div>
  );
}
