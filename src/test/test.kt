class test {
    fun givenJsonArrayOfFoos_whenDeserializingToArray_thenCorrect() {
        val json = "[{"
        intValue
        ":1,"
        stringValue
        ":"
        one
        "}," +
                "{"
        intValue
        ":2,"
        stringValue
        ":"
        two
        "}]"
        val targetArray: Array<Foo> = GsonBuilder().create().fromJson(json, Array<Foo>::class.java)
        assertThat(Lists.newArrayList(targetArray), hasItem(Foo(1, "one")))
        assertThat(Lists.newArrayList(targetArray), hasItem(Foo(2, "two")))
        assertThat(Lists.newArrayList(targetArray), not(hasItem(Foo(1, "two"))))
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(Example::class.java)
    }
}