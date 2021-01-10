# JDrawpad

![logo screenshot](logo-screenshot.png)

**JDrawpad** is an application for designing vector graphics.

It lets the user interactively draw simple vector shapes on a rectangular canvas using a pointing device.
It provides facilities for saving an artwork and opening an existing artwork, as well for saving an artwork to a universal graphic file format (Scalable Vector Graphic or SVG).

## Structure

### Classes

The main JavaFX application class:
- `Main`

Helper classes:
- `DrawingHelper`
  - `Dot`
  - `Arrow`
- `History`

Classes defining event handler methods:
- `DrawingHandlers`

Utility classes:
- `CsvGenerator`
- `SvgGenerator`
- `CsvParser`
- `FileUtils`

Relationship between the top-level classes:

![relationship between the classes](class-relationships.png)

### Descriptions of the classes

#### `Main`

Declared as: `class Main extends javafx.application.Application { ... }`

![diagram of Main](Main.png)

Every JavaFX program must have a class that extends `javafx.application.Application` and overrides its `start(Stage primaryStage)` method.
`primaryStage` represents the primary window in the application.
When a JavaFX application starts up, it creates a root `Stage` (GUI window) object which is passed to the `start(Stage primaryStage)` method of the root class (that extends `Application`) in the JavaFX application.
In our program, `Main` serves the purpose of the root class.
We are also using it to perform the primary UI-related operations.

##### Inner classes

`ToolButton`: Represents a tool button.
It’s a subclass of JavaFX’s `ToggleButton`, so that if it’s included in a `ToggleGroup`, only one of the buttons in that `ToggleGroup` can be selected at a time at most.
The `ToggleGroup` for our toolbar buttons is the field `toolbarGroup`.

#### `History`

Declared as: `public class History { ... }`

![diagram of History](History.png)

The `History` class represents the timeline of the canvas.
JDrawpad uses a `History` for its undo-redo functionality.

#### `DrawingHelper`

Declared as: `public class DrawingHelper { ... }`

![diagram of DrawingHelper](DrawingHelper.png)

This class defines functions that perform the actual drawing commands as well as the related (inner) classes.

#### `DrawingHandlers`

Declared as: `public class DrawingHandlers { ... }`

![diagram of DrawingHandlers](DrawingHandlers.png)

This class defines the event-handler objects that are triggered when user interacts with the canvas using a pointing device while a tool is selected.
