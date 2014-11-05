package pl.mpieciukiewicz.webapp.utils.protectedid

case class ProtectedId(id: String) {
  def realId:Int = IdProtectionUtil.decrypt(id).toInt
}


object ProtectedId {
  def encrypt(id: Int):ProtectedId = ProtectedId(IdProtectionUtil.encrypt(id))
}
