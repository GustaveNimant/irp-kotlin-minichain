package io.ipfs.kotlin.http4k

import kotlin.system.exitProcess
import io.ipfs.kotlin.defaults.*
import io.ipfs.kotlin.url.*
import io.ipfs.kotlin.*

import java.io.File
import java.util.Stack
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

import org.http4k.client.ApacheClient
import org.http4k.client.JavaHttpClient
import org.http4k.client.OkHttp
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.MultipartFormBody
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.core.getFirst
import org.http4k.core.then
import org.http4k.core.toParametersMap
import org.http4k.core.with
import org.http4k.filter.CachingFilters
import org.http4k.filter.DebuggingFilters.PrintRequestAndResponse
import org.http4k.filter.ServerFilters
import org.http4k.hamkrest.hasStatus
import org.http4k.lens.FormField
import org.http4k.lens.Header
import org.http4k.lens.LensFailure
import org.http4k.lens.MultipartForm
import org.http4k.lens.MultipartFormField
import org.http4k.lens.MultipartFormFile
import org.http4k.lens.Validator
import org.http4k.lens.WebForm
import org.http4k.lens.int
import org.http4k.lens.multipartForm
import org.http4k.lens.webForm
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.routing.singlePageApp
import org.http4k.routing.static
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun http4kFormsMultipartLens() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Ex.: --args="-http4k form multipart lens"

    // define fields using the standard lens syntax
    val nameField = MultipartFormField.string().map(::Name, Name::value).required("name")
    val imageFile = MultipartFormFile.optional("image")

    // add fields to a form definition, along with a validator
    val strictFormBody = Body.multipartForm(Validator.Strict, nameField, imageFile, diskThreshold = 5).toLens()

    val server = ServerFilters.CatchAll().then { req: Request ->

    // to extract the contents, we first extract the form and then extract the fields from it using the lenses
    // NOTE: we are "using" the form body here because we want to close the underlying file streams
    strictFormBody(req).use {
	println()
        println(nameField(it))
        println(imageFile(it))
	println()
    }

        Response(OK)
    }.asServer(SunHttp(8000)).start()

    // creating valid form using "with()" and setting it onto the request. The content type and boundary are
    // taken care of automatically
    val multipartform = MultipartForm().with(
        nameField of Name("rita"),
        imageFile of MultipartFormFile("image.txt", ContentType.OCTET_STREAM, "somebinarycontent".byteInputStream()))
    val validRequest = Request(POST, "http://localhost:8000").with(strictFormBody of multipartform)

    println(ApacheClient()(validRequest))

    server.stop()
    exiting(here)
}

fun http4kFormsMultipartStandard() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Ex.: -http4k forms multipart standard
    
    // extract the body from the request and then the fields/files from it
    val server = { req: Request ->
		   val receivedForm = MultipartFormBody.from(req)
		   println()
		   println("$here: server receivedForm")
		   println(receivedForm.fieldValues("field"))
		   println(receivedForm.field("field2"))
		   println(receivedForm.files("file"))
		   println()
		   Response(OK)
    }.asServer(SunHttp(8000)).start()

    // add fields and files to the multipart form body
    val body = MultipartFormBody()
        .plus("field" to "my-value")
        .plus("field2" to MultipartFormField("my-value2", listOf("my-header" to "my-value")))
        .plus("file" to MultipartFormFile("image.txt", ContentType.OCTET_STREAM, "somebinarycontent".byteInputStream()))

    println()
    println("$here: body starts here")
    println(body)
    println("$here: body ends here")
    println()
    
    // we need to set both the body AND the correct content type header on the request
    val request = Request(POST, "http://localhost:8000")
        .header("content-type", "multipart/form-data; boundary=${body.boundary}")
        .body(body)

    println("$here: body.boundary "+body.boundary)
    println("$here: ApacheClient()(request) starts here")
    println(ApacheClient()(request))
    println("$here: ApacheClient()(request) ends here")
    
    server.stop()
 
    exiting(here)
}

fun http4kFormsUnipartLens() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // define fields using the standard lens syntax
    val ageField = FormField.int().required("age")
    val nameField = FormField.map(::Name, Name::value).optional("name")

    // add fields to a form definition, along with a validator
    val strictFormBody = Body.webForm(Validator.Strict, nameField, ageField).toLens()
    val feedbackFormBody = Body.webForm(Validator.Feedback, nameField, ageField).toLens()

    val invalidRequest = Request(GET, "/")
        .with(Header.CONTENT_TYPE of ContentType.APPLICATION_FORM_URLENCODED)

    // the "strict" form rejects (throws a LensFailure) because "age" is required
    try {
        strictFormBody(invalidRequest)
    } catch (e: LensFailure) {
        println(e.message)
    }

    // the "feedback" form doesn't throw, but collects errors to be reported later
    val invalidForm = feedbackFormBody(invalidRequest)
    println(invalidForm.errors)

    // creating valid form using "with()" and setting it onto the request
    val webForm = WebForm().with(ageField of 55, nameField of Name("rita"))
    val validRequest = Request(GET, "/").with(strictFormBody of webForm)

    // to extract the contents, we first extract the form and then extract the fields from it using the lenses
    val validForm = strictFormBody(validRequest)
    val age = ageField(validForm)

    println("$here: age $age")

    exiting(here)
}

fun http4kFormsUnipartStandard() {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    // Ex.: --args="-http4k forms unipart standard"
    
    val request = Request(GET, "/").form("name", "rita").form("age", "55")

    println("$here rita ? "+request.form("name"))
    println("$here age ? "+request.form("age"))
    
    // reparses body every invocation
// Improve   assertEquals("rita", request.form("name"))
// Improve    assertEquals("55", request.form("age"))
// Improve    assertNull(request.form("height"))

    // toParametersMap() gives form as map
    val parameters: Map<String, List<String?>> = request.form().toParametersMap()
    println("$here parameters "+parameters)

// Improve    assertEquals("rita", parameters.getFirst("name"))
// Improve    assertEquals(listOf("55"), parameters["age"])
// Improve    assertNull(parameters["height"])

    exiting(here)
}
fun menuHttp4kFormsMultipartOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    try {
 	val wor = wor_s.pop()
	val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	if(isLoop(here)) println("$here: while wor '$wor'")
	
	when (wor_3) {
	    "sta" -> {http4kFormsMultipartStandard()} 
	    "len" -> {http4kFormsMultipartLens()}
	    else -> {
		fatalErrorPrint ("command were 'sta'ndard or 'len's","'$wor'", "Check input", here)
	    } // else
	} // when (wor_3)
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint ("command were -http4k forms multipart 'sta'ndard or 'len's","no arguments", "Complete input", here)
    }
    exiting(here)
}

fun menuHttp4kFormsOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    try {
 	val wor = wor_s.pop()
	val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	if(isLoop(here)) println("$here: while wor '$wor'")
	
	when (wor_3) {
	    "mul" -> {menuHttp4kFormsMultipartOfWordStack(wor_s)} 
	    "uni" -> {menuHttp4kFormsUnipartOfWordStack(wor_s)} 
	    else -> {
		fatalErrorPrint ("command were 'mul'tipart or 'uni'part","'$wor'", "Check input", here)
	    } // else
	} // when (wor_3)
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint ("command were -http4k forms 'mul'tipart or 'uni'part","no arguments", "Complete input", here)
    }
    exiting(here)
}

fun menuHttp4kFormsUnipartOfWordStack(wor_s: Stack<String>) {
    val (here, caller) = moduleHereAndCaller()
    entering(here, caller)

    if(isTrace(here)) println ("$here: input wor_s '$wor_s'")

    try {
 	val wor = wor_s.pop()
	val wor_3 = threeFirstCharactersOfStringOfCaller(wor, here)
	if(isLoop(here)) println("$here: while wor '$wor'")
	
	when (wor_3) {
	    "sta" -> {http4kFormsUnipartStandard()} 
	    "len" -> {http4kFormsUnipartLens()}
	    else -> {
		fatalErrorPrint ("command were 'sta'ndard or 'len's","'$wor'", "Check input", here)
	    } // else
	} // when (wor_3)
    } // try
    catch (e: java.util.EmptyStackException) {
	fatalErrorPrint ("command were -http4k forms unipart 'sta'ndard or 'len's","no arguments", "Complete input", here)
    }
    exiting(here)
}
