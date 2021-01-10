# [AppName]

[AppName] is an application for designing vector graphics.

It lets the user interactively draw simple vector shapes on a rectangular canvas using a pointing device.
It provides facilities for saving an artwork and opening an existing artwork as well as saving an artwork to a universal graphic file format (Scalable Vector Graphic or SVG).

## Structure

### Classes

The main JavaFX application class:
- `Main`

Helper classes:
- `DrawingHelper`
  - `Dot`
  - `Arrow`
- `History`

Classes defining implementing event handler methods:
- `UIHandlers`
- `DrawingHandlers`

Utility classes:
- `CsvGenerator`
- `SvgGenerator`
- `CsvParser`
- `FileUtils`

### Relationship between the classes

[class relationship image]

### Descriptions of the classes

#### `Main`

`class Main extends javafx.application.Application`

Every JavaFX program must have a class that extends `javafx.application.Application` and overrides its `start(Stage primaryStage)` method.
`primaryStage` represents the primary window in the application.
When a JavaFX application starts up, it creates a root `Stage` (GUI window) object which is passed to the `start(Stage primaryStage)` method of the root class (that extends `Application`) in the JavaFX application.
In our program, `Main` serves the purpose of the root class.

##### Inner classes

`ToolButton`: Represents a tool button.
It’s a subclass of JavaFX’s `ToggleButton`, so that if it’s included in a `ToggleGroup`, only one of the buttons in that `ToggleGroup` can be selected at a time at most.
The `ToggleGroup` for our toolbar buttons is the field `toolbarGroup`.

#### `History`

`public class History`

The `History` class represents the timeline of the canvas.
[AppName] uses a `History` for its undo-redo functionality.

#### `UIHandlers`

#### `DrawingHandlers`