import { useState } from 'react'

interface Props {
  result: string
  processingTimeMs: number | null
  error: string | null
}

export function ResultPanel({ result, processingTimeMs, error }: Props) {
  const [copied, setCopied] = useState(false)

  function handleCopy() {
    if (!result) return
    navigator.clipboard.writeText(result).then(() => {
      setCopied(true)
      setTimeout(() => setCopied(false), 2000)
    })
  }

  return (
    <div className="bg-gray-800 rounded-xl shadow-lg p-6 flex flex-col gap-4">
      <div className="flex items-center justify-between">
        <h2 className="text-lg font-semibold text-gray-200">Result</h2>
        {result && (
          <button
            onClick={handleCopy}
            className="text-xs px-3 py-1.5 rounded-lg border border-gray-600
                       text-gray-400 hover:text-gray-200 hover:border-gray-400 transition-colors"
          >
            {copied ? '✅ Copied!' : '📋 Copy'}
          </button>
        )}
      </div>

      {error && (
        <div className="bg-red-900/40 border border-red-700 text-red-300 rounded-lg p-3 text-sm">
          {error}
        </div>
      )}

      <div
        className={`min-h-[9rem] bg-gray-900 rounded-lg p-4 font-mono text-sm break-all
                    border border-gray-700 whitespace-pre-wrap
                    ${result ? 'text-green-300' : 'text-gray-600'}`}
      >
        {result || 'Result will appear here…'}
      </div>
    </div>
  )
}
