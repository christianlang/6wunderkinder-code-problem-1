open FSharp.Data
open FSharp.Data.JsonExtensions
open System.Text
open System.Net
open System.Net.Http

let consumerKey = "SECRET"
let consumerSecret = "SECRET"

type OAuthResult = JsonProvider<"oauth2-token.json">
type SearchResult = JsonProvider<"search-tweets.json">

let encode (source:string) =
    let bytes = Encoding.UTF8.GetBytes(source)
    System.Convert.ToBase64String(bytes)

let authenticate =
    async {
        let req = WebRequest.CreateHttp("https://api.twitter.com/oauth2/token")
        req.Method <- "POST"
        req.Headers.["Authorization"] <- "Basic " + encode(consumerKey + ":" + consumerSecret)
        req.ContentType <- "application/x-www-form-urlencoded"

        let body = "grant_type=client_credentials"
        req.GetRequestStream().Write(Encoding.UTF8.GetBytes(body), 0, Encoding.UTF8.GetByteCount(body))
        
        let! res = req.AsyncGetResponse()
        return OAuthResult.Load(res.GetResponseStream()).AccessToken
    }

let search query token =
    async {
        let req = WebRequest.CreateHttp("https://api.twitter.com/1.1/search/tweets.json?count=100&q=" + query)
        req.Headers.["Authorization"] <- "Bearer " + token

        let! res = req.AsyncGetResponse()
        return SearchResult.Load(res.GetResponseStream()).Statuses
    }

let formatTweet (tweet:SearchResult.Status) =
    tweet.User.ScreenName + ": " + tweet.Text

[<EntryPoint>]
let main argv = 
    authenticate
    |> Async.RunSynchronously
    |> search "6Wunderkinder"
    |> Async.RunSynchronously
    |> Array.map formatTweet
    |> String.concat "\n"
    |> printfn "%A"
    0 // return an integer exit code
