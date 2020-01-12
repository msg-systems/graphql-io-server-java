(async () => {

    /*
    **  ==== CLIENT ====
    */

    const { Client } = require("graphql-io-client")
    const client = new Client({ url: "http://localhost:8080", debug: 9 })
    client.at("debug", (ev) => console.log(`client [${ev.level}] ${ev.msg}`))
    await client.connect()
    let result = await client.query("{ hello }")
    await client.disconnect()
    console.log(result.data)

    await server.stop()
    console.log("OK")

})().catch((err) => {
    console.log("ERROR", err)
})
