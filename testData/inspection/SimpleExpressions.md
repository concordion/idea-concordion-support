### Test spec

[simple](- "?=data()")
[complex](- "?=makeItComplex().data()")

[simple](- "c:set=#var")
[invalid](- "c:set=assignHere()")

[simple](- "c:set=#var = data()")
[complex](- "c:set=#var = makeItComplex().data()")

[simple](- "#var = data()")
[complex](- "#var = makeItComplex().data()")
