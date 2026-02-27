package scrapy

import (
	"fmt"
	"net/http"

	"github.com/PuerkitoBio/goquery"
)

func FetchData(url string) (string, string, error) {
	return fetchData(url)
}

func fetchData(url string) (string, string, error) {
	client := &http.Client{}

	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return "", "", err
	}

	req.Header.Set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")

	resp, err := client.Do(req)
	if err != nil {
		return "", "", err
	}

	defer resp.Body.Close()

	if resp.StatusCode != 200 {
		return "", "", fmt.Errorf("status code error: %d %s", resp.StatusCode, resp.Status)
	}

	doc, err := goquery.NewDocumentFromReader(resp.Body)
	if err != nil {
		return "", "", err
	}

	title := doc.Find("head title").Text()

	description, exists := doc.Find("head meta[name='description']").Attr("content")
	if exists {
		return title, description, nil
	}

	return title, "", nil
}
