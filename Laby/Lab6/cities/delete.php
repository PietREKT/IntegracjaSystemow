<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
include_once '../config/Database.php';
include_once '../class/Cities.php';
$database = new Database();
$db = $database->getConnection();
$cities = new Cities($db);
$cities->id = $_REQUEST["id"];
$cities->delete();

http_response_code(200);
echo json_encode(array("message" => "City was deleted."));
