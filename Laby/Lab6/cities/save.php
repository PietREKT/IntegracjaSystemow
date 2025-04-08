<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
include_once '../config/Database.php';
include_once '../class/Cities.php';
$database = new Database();
$db = $database->getConnection();
$cities = new Cities($db);

$input = json_decode(file_get_contents("php://input"), true);
$cities->countrycode = $input["countrycode"];
$cities->district = $input["district"];
$cities->population = $input["population"];
$cities->name = $input["name"];
$cities->save();

http_response_code(200);
echo json_encode($cities);
