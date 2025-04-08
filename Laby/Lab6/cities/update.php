<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
include_once '../config/Database.php';
include_once '../class/Cities.php';
$database = new Database();
$db = $database->getConnection();
$cities = new Cities($db);

$input = json_decode(file_get_contents("php://input"), true);
$cities->id = $_REQUEST["id"];
$cities->update($input["name"], $input["countrycode"], $input["district"], $input["population"]);

http_response_code(200);
echo json_encode(array("status" => "success", "message" => "City updated successfully."));