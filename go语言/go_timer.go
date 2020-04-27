package main

import (
	"fmt"
	"time"
)

func init() {
	fmt.Println("init")
}

func main() {

	ticker := time.NewTicker(2 * time.Second)
	boom := time.After(10 * time.Second)
	for {

		select {
		case <-ticker.C:
			fmt.Println("logging")
		case <-boom:
			fmt.Println("boom")
			break
		}
	}

}
