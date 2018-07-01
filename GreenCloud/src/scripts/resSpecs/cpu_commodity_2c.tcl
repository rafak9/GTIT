set resSpec [new ResourceSpec]

$resSpec set-name "Commodity processor 2 cores"
$resSpec set-type "Computing"
$resSpec set-arch 1
$resSpec add-capacity 2000600
$resSpec add-capacity 2000600
$resSpec add-power-state 1


#source "powerModels/component/301W033idle.tcl"
source "powerModels/component/335W010idle.tcl"
$resSpec set-power-model $pModel
