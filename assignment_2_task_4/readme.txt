Assigment 2 - Task 4:

We decided to implement configuration via singleton class named Conf.
The object has different boolean variables for the optional features: encryption, message color, logging to file, logging to console and Server GUI.
The decision to use an object rather than parameter passing was made because we were able to make the code more clean and to provide a single source of configuration (by calling the getInstance method).

The Conf class has a set of default values in case the user forgets to create a config.json or forgets any of the configuration parameters.
All the feature selection conbinations are valid.

We decided to to implement variability for the Server GUI, Color, Encryption, Logging [File Logging, StdOut logging].
We left passowrd authentication as a requirement as we felt it is a bare-minimum security feature our potential product should have.