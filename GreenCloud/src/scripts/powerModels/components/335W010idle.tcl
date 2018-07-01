set pModel [new LinearPModel]

set unique_name "Linear model: 135W max, idle 10% of max 13.5 121.5 "
$pModel set-name $unique_name

$pModel set-coef-number 2
$pModel set-coefficient-numeric "0" 13.5 
$pModel set-coefficient "Intercept" 121.5

if { $printPModel == 1 } {
puts -nonewline "Loaded: Power Model: "
puts $unique_name
}
