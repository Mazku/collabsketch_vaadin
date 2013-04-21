# Collaborative Sketch Canvas for Vaadin

The widget draws the same collaborative sketching canvas for multiple users and each of them will be assigned with their own color. When anyone of the users edit the sketch its automaticly pushed for all the other users.

When you use this component you should handle CollabSketchLineContainer-class and share it between each instance of the server-side component. If you want to share the canvas for all the users you should make the CollabSketchLineContainer-instance as singleton.

### Dependencies

- Vaadin 7.1

### Release notes

This widget is in alpha state, but the main features:
- Concurrent and collaborative use of HTML Canvas for Chrome
- Automatic push without need of client side activity
- Selecting the size of the canvas
- Touch handling for mobile devices
- Clearing the whole canvas

### License & Author

Apache License 2.0

Matias Sundberg