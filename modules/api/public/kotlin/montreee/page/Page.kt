package montreee.page

import montreee.data.DataRepository
import montreee.data.DataStore

data class Page(val data: DataStore<Any> = DataStore(DataRepository()))
