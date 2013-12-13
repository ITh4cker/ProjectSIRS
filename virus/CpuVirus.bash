#!/bin/bash
loop_func() {
	while [ true ]
	do
		echo "banana" > /dev/null
	done
}

loop_func &
