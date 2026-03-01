package main

import (
	"log"
	"net/http"

	"github.com/DER-2SH-KA/Parkle/backendGo/website_scraper/src/scrapy"
	"github.com/gin-gonic/gin"
)

const (
	WEBSITE_API string = "/api/websites"
	HOST        string = "0.0.0.0"
	PORT        string = "9001"
)

type WebsiteDataResponseDto struct {
	Title       string `json:"title"`
	Description string `json:"description"`
}

type ErrorResponseDto struct {
	MessageForClient string `json:"messageForClient"`
	MessageForDev    string `json:"messageForDev"`
}

func init() {
	gin.SetMode(gin.ReleaseMode)
}

func main() {
	router := gin.Default()

	router.GET("/", func(c *gin.Context) {
		c.JSON(200, gin.H{"hello": "world"})
	})
	router.GET(WEBSITE_API+"/fetch/data", fetchWebsiteData)

	log.Fatal(router.Run(HOST + ":" + PORT))
}

func fetchWebsiteData(c *gin.Context) {
	website := &WebsiteDataResponseDto{
		Title:       "",
		Description: "",
	}

	url := c.Query("url") // Param from request.
	if url == "" {
		log.Println("url required!")

		c.AbortWithStatusJSON(
			http.StatusBadRequest,
			&ErrorResponseDto{
				MessageForClient: "Не передан URL для получения информации!",
				MessageForDev:    "url required for fetching data",
			},
		)

		return
	}

	log.Printf("url: %s\n", url)

	title, description, err := scrapy.FetchData(url)
	if err != nil {
		log.Printf("Error fetching data for url '%s': %s\n", url, err)

		c.AbortWithStatusJSON(
			http.StatusUnprocessableEntity,
			&ErrorResponseDto{
				MessageForClient: "Ошибка получения информации по переданному URL!",
				MessageForDev:    err.Error(),
			},
		)

		return
	}

	website.Title = title
	website.Description = description

	log.Printf("Fetched data for url '%s'\n", url)
	c.JSON(http.StatusOK, &website)
}
