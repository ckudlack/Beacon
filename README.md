# Beacon

## V1 (Proof-of-concept)

I originally created this app to alleviate my parents' worries while I was traveling through SE Asia for 3 months. They wanted to
have some record of my location in case something went wrong and they couldn't get ahold of me.

Beacon works like this: While I was traveling, a JobService was scheduled to run in the background every 12 hours which would
query my location and then send that information to Firebase. That information could be retrieved by my family in a map
on their phone, so that they could see the latitude, longitude, and the time that the location was sent. If I had no internet
connection when the Service awoke, which happened frequently, it would wait to send the location information until the next time
I connected to WiFi.

In addition to alleviating my parents' anxieties, it also took the responsibility off of me to manually check in with them, since the app
would perform this duty asynchronously. A nice bonus was that after the the trip was done, I had a nice map of my travels with
the time and date I was in each location!

## V2

Now that I'm back and have more time to improve the app, I'm working on these new features:

- Ability to add multiple trips
- Ability to see which trip is actively broadcasting
- DB support for multiple users, multiple trips
- Ability to share your trip with others ("Observers") through an email address
- General UI improvements

