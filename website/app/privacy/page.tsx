import Link from "next/link";
export const metadata = { title: "Privacy Policy | The Mute Master" };

export default function PrivacyPolicy() {
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
          Privacy Policy
        </h1>

        <p className="mb-2">
          <strong>Application Name:</strong> MuteMaster - Auto Silent Manager
        </p>
        <p className="mb-2">
          <strong>Developer:</strong> Individual Developer
        </p>
        <p className="mb-6">
          <strong>Effective Date:</strong> January 23, 2026
        </p>

        <p className="mb-8">
          This Privacy Policy applies to the MuteMaster app (hereby referred to
          as &quot;Application&quot;) for mobile devices. The service is provided for free
          and is intended for use as is.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          1. Prominent Disclosure: Background Location Access
        </h2>
        <p className="mb-4">
          To function as intended, this Application requires access to your
          location data even when you are not directly interacting with the app.
        </p>

        <div className="border-2 border-foreground p-5 my-6 font-bold bg-foreground/5 rounded-lg">
          The MuteMaster collects location data to enable the &apos;Automatic Mute&apos;
          feature, which silences your device when you enter specific zones
          (like your office or school), even when the app is closed or not in
          use.
        </div>

        <p className="mb-8">
          This data is processed locally on your device to trigger silent mode
          events. If you decline this permission, the automatic geofencing
          feature will not function.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          2. Information Collection and Use
        </h2>
        <p className="mb-4">
          For a better experience, while using our Service, the Application
          requires certain personally identifiable information and permissions.
          We strictly categorize our data collection as follows:
        </p>

        <h3 className="text-xl font-medium mt-6 mb-3">
          A. Location Data (The &quot;What, Why, When&quot;)
        </h3>
        <ul className="list-disc pl-6 mb-6 space-y-2">
          <li>
            <strong>What we collect:</strong> Precise geolocation data
            (Latitude/Longitude).
          </li>
          <li>
            <strong>Why we need it:</strong> To create and monitor &quot;Geofences&quot;
            (virtual perimeters) around locations you define (e.g., Office,
            Church, Library).
          </li>
          <li>
            <strong>When it is used:</strong>
            <ul className="list-circle pl-6 mt-2 space-y-1">
              <li>
                <strong>Foreground:</strong> When you are adding a new location
                pin on the map.
              </li>
              <li>
                <strong>Background:</strong> When the app is closed or
                minimized, the Android OS monitors these Geofences to wake the
                app up solely for the purpose of changing your ringer mode.
              </li>
            </ul>
          </li>
        </ul>

        <h3 className="text-xl font-medium mt-6 mb-3">B. Device State & Other Permissions</h3>
        <ul className="list-disc pl-6 mb-8 space-y-4">
          <li>
            <strong>Foreground Services:</strong> We utilize persistent foreground services to reliably monitor your geofences and schedules without the Android or Huawei Operating Systems closing the app in the background.
          </li>
          <li>
            <strong>Do Not Disturb (DND) Access (ACCESS_NOTIFICATION_POLICY):</strong> Read/Write access to Ringer and Media volume settings. We require Do Not Disturb access to completely silence the device or manage priority interruptions during scheduled or geofenced events.
          </li>
          <li>
            <strong>Notifications (POST_NOTIFICATIONS):</strong> Permission to post status bar alerts. Used to inform you when the app has automatically changed your ringer mode.
          </li>
          <li>
            <strong>Exact Alarms (SCHEDULE_EXACT_ALARM, USE_EXACT_ALARM):</strong> Permission to schedule precise, time-critical alarms. Used to power the Time-Based Muting feature.
          </li>
          <li>
            <strong>Boot on Start (RECEIVE_BOOT_COMPLETED):</strong> Permission to run code when the device restarts. Used to automatically restore and reschedule all your active geofences and time-based schedules.
          </li>
          <li>
            <strong>Schedule & Preference Data:</strong> Schedule names, start/end times, active days of week, and custom sound profile settings. Saved locally to your device's database whenever you create or edit a schedule. Never transmitted off-device.
          </li>
        </ul>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          3. Data Storage and Retention (Local Only Policy)
        </h2>
        <p className="mb-4">
          We are committed to a &quot;Local First&quot; architecture.
        </p>
        <ul className="list-disc pl-6 mb-8 space-y-2">
          <li>
            <strong>No Cloud Storage:</strong> Your location history, saved
            zones, and preferences are stored locally in your device&apos;s internal
            database (SQLite, Shared Preferences, and DataStore). We do not operate a backend
            server to store user data.
          </li>
          <li>
            <strong>No Data Transmission:</strong> We do not transmit your
            coordinates to any external server, analytics platform, or third
            party. Your defined Geofence locations are securely registered with your device's native operating system (Google Play Services or Huawei Mobile Services) strictly to enable the background trigger, but they never leave your device.
          </li>
          <li>
            <strong>Retention:</strong> Since data is local, it is retained only
            as long as the Application is installed on your device. Uninstalling
            the Application permanently erases all saved data.
          </li>
        </ul>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          4. Third-Party Services
        </h2>
        <p className="mb-4">
          The Application uses third-party services that may collect information
          used to identify you, including your device&apos;s <strong>Advertising ID</strong>. We utilize these services strictly for
          functional infrastructure (Google Play Services / Huawei Mobile Services for Maps and Geofencing APIs), as well as app
          stability and usage analytics.
        </p>
        <p className="mb-2">
          Link to privacy policy of third-party service providers used by the
          app:
        </p>
        <ul className="list-disc pl-6 mb-8 space-y-2">
          <li>
            <strong>Google Play Services:</strong>{" "}
            <a
              href="https://policies.google.com/privacy"
              className="text-primary hover:underline"
              target="_blank"
              rel="noopener noreferrer"
            >
              https://policies.google.com/privacy
            </a>
          </li>
          <li>
            <strong>Huawei Mobile Services (HMS Core / Location Kit):</strong>{" "}
            <a
              href="https://consumer.huawei.com/en/privacy/privacy-policy/"
              className="text-primary hover:underline"
              target="_blank"
              rel="noopener noreferrer"
            >
              https://consumer.huawei.com/en/privacy/privacy-policy/
            </a>
          </li>
          <li>
            <strong>Google Analytics for Firebase:</strong>{" "}
            <a
              href="https://firebase.google.com/policies/analytics"
              className="text-primary hover:underline"
              target="_blank"
              rel="noopener noreferrer"
            >
              https://firebase.google.com/policies/analytics
            </a>
          </li>
          <li>
            <strong>Firebase Crashlytics:</strong>{" "}
            <a
              href="https://firebase.google.com/support/privacy"
              className="text-primary hover:underline"
              target="_blank"
              rel="noopener noreferrer"
            >
              https://firebase.google.com/support/privacy
            </a>
          </li>
        </ul>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          5. Government Compliance & Legal Rights
        </h2>

        <h3 className="text-xl font-medium mt-6 mb-3">
          GDPR Data Protection Rights (European Union)
        </h3>
        <p className="mb-4">
          If you are a resident of the European Economic Area (EEA), you have
          certain data protection rights. As MuteMaster stores data locally:
        </p>
        <ul className="list-disc pl-6 mb-6 space-y-2">
          <li>
            <strong>Right to Access/Rectification:</strong> You can view and
            edit your data directly within the App interface.
          </li>
          <li>
            <strong>Right to Erasure:</strong> You can delete specific locations
            within the app or uninstall the app to erase all data.
          </li>
          <li>
            <strong>Right to Restrict Processing:</strong> You can revoke
            Location Permissions in your Android Settings at any time.
          </li>
        </ul>

        <h3 className="text-xl font-medium mt-6 mb-3">
          CCPA/CPRA Privacy Rights (California, USA)
        </h3>
        <p className="mb-4">
          Under the California Consumer Privacy Act (CCPA) and California
          Privacy Rights Act (CPRA):
        </p>
        <ul className="list-disc pl-6 mb-6 space-y-2">
          <li>
            <strong>Do Not Sell My Personal Information:</strong> We do not
            sell, trade, or rent your personal identification information to
            others. We have never sold your data and will never sell your data.
          </li>
          <li>
            <strong>Right to Know:</strong> You have the right to request
            details about the categories of data collected (outlined in Section
            2).
          </li>
        </ul>

        <h3 className="text-xl font-medium mt-6 mb-3">
          Children’s Privacy (COPPA)
        </h3>
        <p className="mb-8">
          These Services do not address anyone under the age of 13. We do not
          knowingly collect personally identifiable information from children
          under 13. Since our app does not require a login or account creation,
          we do not obtain age information. If you are a parent or guardian and
          you are aware that your child has provided us with personal
          information, please contact us so that we will be able to perform
          necessary actions.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          6. Security
        </h2>
        <p className="mb-8">
          We value your trust in providing us your Personal Information.
          However, remember that no method of electronic storage is 100% secure
          and reliable, and we cannot guarantee its absolute security. We
          utilize standard Android security sandboxing to ensure other apps
          cannot read your MuteMaster data.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          7. Changes to This Privacy Policy
        </h2>
        <p className="mb-8">
          We may update our Privacy Policy from time to time. Thus, you are
          advised to review this page periodically for any changes. We will
          notify you of any changes by posting the new Privacy Policy on this
          page.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 border-b border-foreground/50 pb-2">
          8. Grievance Officer & Contact
        </h2>
        <p className="mb-4">
          In accordance with the Information Technology Act (India) and DPDP compliance, if you have questions regarding your data, please contact:
        </p>
        <ul className="list-none pl-0 mb-8 space-y-2">
          <li><strong>Name:</strong> Dipankaj</li>
          <li>
            <strong>Email:</strong>{" "}
            <a
              href="mailto:dipankajsingh25@gmail.com"
              className="text-primary hover:underline"
            >
              dipankajsingh25@gmail.com
            </a>
          </li>
          <li><strong>Location:</strong> New Delhi, India</li>
        </ul>
      </div>
    </div>
  );
}
