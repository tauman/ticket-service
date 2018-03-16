# Ticket Service

This is an implemtation of the `TicketService` interface, with an in-memory implementation of the backing storage along with all of the supporting classes.


## Assumptions

There were several assumptions made for the implementation:
+ The Venue is rectangular, with the same number of seats in each row.
+ Seat assignment is all or nothing, so if there is not an adequate number of seats for a hold, no seats will be held.
+ No special seat assignment strategy was necessary.
+ The implementation will not need to be clustered.


## Running the Tests

Since this is only an implementation of a service and its backend, this is not really an application to run. Although there is a main class that can be executed, it will only print out a message with simple instructions for runnng the tests. This application is a `gradle` project with no custom tasks. Acquiring and executing the tests is fairly simple:

1. Ensure that `Java 8` and `gradle` are installed so that `gradle` can be executed from the command line.
2. Clone the project to an empty directory via `git clone`.
3. run the tests:
    + `gradle test` will build and execute all of the tests, with some output to the console. However, this will only run once unless there are code changes.
    + `gradle cleanTest test` will force a re-run of all the tests every time.
    + Adding the `--info` flag to either of the above commands show verbose output to the execution, including application logging.
    + Once the tests are run, the test report can be found at `<PROJECT_ROOT>/build/reports/tests/test/index.html`.

The tests themselves are mostly _unit_ tests, with the exception of the tests for the TicketService implementation, which I would consider to be _integration_ tests. However, since this is a small project and everything is in-memory, all are run together.


## Thoughts on Potential Enhancements

A quick look at the `TicketService` implementation will show that it is rather trivial and delegates nearly all functionality to the `Venue` class. This allows the service to acquire seats without any knowledge of the underlying structure or configuration of the seats in the Venue.

The stragegy used to find available seats it unsophisticated and naive. It starts at the front row and moves through each seat of each row, finding each available seat until the number of requested seats is found. Obviously, this would be inadequate for most real-world implementations except as a last-resort. An enhanced version of this application could be written where the strategy is abstracted into an interface so that the service could makes calls to the venue such that it also specifies the strategy. For example, it could specify "contiguous-first" so that preference is given to finding seats in a contiguous block.
