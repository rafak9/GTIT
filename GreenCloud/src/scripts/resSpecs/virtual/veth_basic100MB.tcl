set resSpec [new ResourceSpec]

$resSpec set-name "Nominal 100MB virtual NIC"
$resSpec set-type "Networking"
$resSpec set-arch 1
$resSpec add-capacity 100000
$resSpec add-power-state 1
