# Travel Data Manager system 

This Java program processes and stores data about trips, including trip ID, city, date, number of days, price, and type of vehicle. The information is stored in a text file named `db.csv`.

## Features

- **View File Content (`print`)**: Displays the content of `db.csv` in a table format.
- **Add New Trip (`add`)**: Adds new trip information to `db.csv`.
- **Delete Trip (`del`)**: Deletes a trip from `db.csv` by ID.
- **Edit Trip (`edit`)**: Edits trip information in `db.csv`.
- **Sort Trips (`sort`)**: Sorts trips in `db.csv` by date.
- **Find Trips (`find`)**: Finds trips in `db.csv` with a price not exceeding a specified value.
- **Calculate Average Price (`avg`)**: Calculates the average price of trips in `db.csv`.
- **Exit Program (`exit`)**: Exits the program, saving any changes to `db.csv`.

## Usage

### Commands

#### Add New Trip

To add a new trip, use the `add` command followed by the trip details separated by semicolons.

##### Example

```
add 101;Daugavpils;03/07/2021;5;150.50;TRAIN
```

#### Output

```
added
```

#### Delete Trip

To delete a trip, use the `del` command followed by the trip ID.

##### Example

```
del 101
```

#### Output

```
deleted
```

#### Edit Trip

To edit a trip, use the `edit` command followed by the trip ID and the details to be edited.

##### Example

```
edit 101;Riga;05/08/2021;7;200.75;BUS
```

#### Output

```
changed
```

#### Sort Trips

To sort trips by date, use the `sort` command.

##### Example

```
sort
```

#### Output

```
sorted
```

#### Find Trips

To find trips with a price not exceeding a specified value, use the `find` command followed by the price.

##### Example

```
find 300.00
```

#### Output

```
ID   City       Date       Days  Price   Vehicle
101  Riga       05/08/2021   7   200.75  BUS
```

#### Calculate Average Price

To calculate the average price of trips, use the `avg` command.

##### Example

```
avg
```

#### Output

```
average=200.75
```

#### Print File Content

To print the content of `db.csv`, use the `print` command.

##### Example

```
print
```

#### Output

```
ID   City       Date       Days  Price   Vehicle
101  Riga       05/08/2021   7   200.75  BUS
```

#### Exit Program

To exit the program, use the `exit` command.

##### Example

```
exit
```

### Prerequisites

- Java Development Kit (JDK) installed on your system.
- A code editor or IDE (such as IntelliJ IDEA or Eclipse).
- The `db.csv` file must be in the same directory as `Main.java`.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Additional Notes

- The `db.csv` file should be located in the same directory as `Main.java`.
- The program handles incorrect user inputs and provides relevant error messages.
- Ensure all data in `db.csv` is correctly formatted as per the specifications.

### Example `db.csv` Format

```
101;Daugavpils;03/07/2021;5;150.50;TRAIN
102;Rome;15/05/2021;7;300.00;BUS
```
