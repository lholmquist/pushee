package org.aerogear.connectivity.api;

import java.util.Set;

import org.aerogear.connectivity.model.AndroidVariant;
import org.aerogear.connectivity.model.SimplePushVariant;
import org.aerogear.connectivity.model.iOSVariant;

/**
 * Logical construct of an overall mobile and push-enabled Application
 */
public interface PushApplication {
    
    /**
     * The name of the application. 
     */
    void setName(final String name);
    String getName();

    /**
     * The description of the app.
     */
    void setDescription(final String description);
    String getDescription();

    /**
     * Identifier used to register variants with this PushApplication
     */
    void setPushApplicationID(String pushApplicationID);
    String getPushApplicationID();

    /**
     * The collection of iOS Variants. 
     */
    void setIOSApps(final Set<iOSVariant> iOSApps);
    Set<iOSVariant> getIOSApps();

    /**
     * The collection of Android Variants. 
     */
    void setAndroidApps(final Set<AndroidVariant> androidApps);
    Set<AndroidVariant> getAndroidApps();

    /**
     * The collection of SimplePush Variants. 
     */
    void setSimplePushApps(final Set<SimplePushVariant> simplePushApps);
    Set<SimplePushVariant> getSimplePushApps();

}
