<?php
// Author: Colton Thompson <colton.thompson1@snhu.edu>
// Date Created: March 30th, 2021
// This creates a simple leaderboard that displays and organizes scores in a descending order from a database
$HOST = "localhost";
$SQL_USER = "root";
$SQL_PASS = "";
$DB = "leaderboard";

$connect = mysqli_connect($HOST, $SQL_USER, $SQL_PASS, $DB);

// Check for SQL Errors
if (mysqli_connect_errno())
{
  echo "SQL Error - " . mysqli_connect_error();
  exit();
}

mysqli_select_db($connect, $DB);
 ?>


<html>
<head>
  <title>Leaderboard - By Colton</title>
</head>

<body>
<h3><center>Leaderboard</center></h3>
<?php
// This query will pull the data from the table and sort it by the score value in a descending order
$query = "SELECT * FROM `scores` ORDER BY `score` DESC LIMIT 50;";
$result = mysqli_query($connect, $query);

if ($result) {
  // Generate the basic HTML structure to display the leaderboard data
  echo '<table>
        <thead>
          <tr>
            <th>#</th>
            <th>User</th>
            <th>Score</th>
          </tr>
        </thead>';
  echo '<tbody>';
  $count = 0;
  while ($row = $result->fetch_assoc()) {
    // Assign a rank value by counting the rows
    $rank = $count + 1;
    // Pull the username and score from the row data
    $user = $row['user'];
    $score = $row['score'];

    // Display the row
    echo '
    <tr>
      <td>' . $rank . '</td>
      <td>' . $user . '</td>
      <td>'. $score . '</td>
      </tr>';

      // Increase the count
      $count++;
  }
  echo '</tbody></table>';
} else {
  echo 'No data to display!';
}
?>

</body>
