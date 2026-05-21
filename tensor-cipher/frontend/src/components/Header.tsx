export function Header() {
  return (
    <header className="text-center py-10">
      <div className="flex items-center justify-center gap-3 mb-2">
        <span className="text-4xl">🔐</span>
        <h1 className="text-4xl font-bold bg-gradient-to-r from-indigo-400 to-purple-400 bg-clip-text text-transparent">
          Tensor Cipher
        </h1>
      </div>
      <p className="text-gray-400 text-sm tracking-widest uppercase">
        3rd-rank tensor encryption · 4×4×4
      </p>
    </header>
  )
}
