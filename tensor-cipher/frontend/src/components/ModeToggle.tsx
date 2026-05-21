import type { Mode } from '../types/cipher'

interface Props {
  mode: Mode
  onChange: (mode: Mode) => void
}

export function ModeToggle({ mode, onChange }: Props) {
  return (
    <div className="flex rounded-lg overflow-hidden border border-gray-700 w-fit">
      {(['encrypt', 'decrypt'] as Mode[]).map((m) => (
        <button
          key={m}
          onClick={() => onChange(m)}
          className={`px-5 py-2 text-sm font-medium transition-colors capitalize ${
            mode === m
              ? 'bg-indigo-600 text-white'
              : 'bg-gray-800 text-gray-400 hover:text-gray-200 hover:bg-gray-700'
          }`}
        >
          {m === 'encrypt' ? '🔒 Encrypt' : '🔓 Decrypt'}
        </button>
      ))}
    </div>
  )
}
