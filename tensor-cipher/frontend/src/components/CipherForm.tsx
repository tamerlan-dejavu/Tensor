import type { Mode } from '../types/cipher'
import { ModeToggle } from './ModeToggle'

interface Props {
  inputText: string
  keyValue: string
  isLoading: boolean
  mode: Mode
  onTextChange: (v: string) => void
  onKeyChange: (v: string) => void
  onModeChange: (m: Mode) => void
  onSubmit: () => void
}

export function CipherForm({
  inputText, keyValue, isLoading, mode,
  onTextChange, onKeyChange, onModeChange, onSubmit,
}: Props) {
  return (
    <div className="bg-gray-800 rounded-xl shadow-lg p-6 flex flex-col gap-5">
      <div className="flex items-center justify-between flex-wrap gap-3">
        <h2 className="text-lg font-semibold text-gray-200">Input</h2>
        <ModeToggle mode={mode} onChange={onModeChange} />
      </div>

      <textarea
        value={inputText}
        onChange={(e) => onTextChange(e.target.value)}
        rows={6}
        placeholder={mode === 'encrypt' ? 'Enter text to encrypt…' : 'Paste ciphertext to decrypt…'}
        className="w-full resize-y bg-gray-900 text-gray-100 rounded-lg p-3 text-sm font-mono
                   border border-gray-700 focus:outline-none focus:border-indigo-500
                   placeholder:text-gray-600 transition-colors"
      />

      <div className="relative">
        <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500 select-none">🔑</span>
        <input
          type="text"
          value={keyValue}
          onChange={(e) => onKeyChange(e.target.value)}
          placeholder="Encryption key"
          className="w-full bg-gray-900 text-gray-100 rounded-lg pl-9 pr-4 py-3 text-sm
                     border border-gray-700 focus:outline-none focus:border-indigo-500
                     placeholder:text-gray-600 transition-colors"
        />
      </div>

      <button
        onClick={onSubmit}
        disabled={isLoading}
        className="flex items-center justify-center gap-2 bg-indigo-600 hover:bg-indigo-500
                   disabled:opacity-50 disabled:cursor-not-allowed
                   text-white font-semibold rounded-lg py-3 transition-colors"
      >
        {isLoading ? (
          <>
            <span className="inline-block w-4 h-4 border-2 border-white/30 border-t-white
                             rounded-full animate-spin" />
            Processing…
          </>
        ) : (
          mode === 'encrypt' ? '🔒 Encrypt' : '🔓 Decrypt'
        )}
      </button>
    </div>
  )
}
