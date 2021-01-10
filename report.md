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


