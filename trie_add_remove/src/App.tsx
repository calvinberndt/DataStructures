import { useState, useCallback } from 'react';

class TrieNode {
  constructor() {
    this.children = new Map();
  }
  
  addNewChild(char) {
    const newNode = new TrieNode();
    this.children.set(char, newNode);
    return newNode;
  }
  
  getChild(char) {
    return this.children.get(char) || null;
  }
  
  getChildCount() {
    return this.children.size;
  }
  
  removeChild(char) {
    return this.children.delete(char);
  }
}

class Trie {
  constructor() {
    this.root = new TrieNode();
  }
  
  insert(str) {
    const steps = [];
    let node = this.root;
    steps.push({ action: 'start', message: `Starting insertion of "${str}"`, node: 'root', char: null });
    
    for (let i = 0; i < str.length; i++) {
      const char = str[i];
      let child = node.getChild(char);
      if (child === null) {
        child = node.addNewChild(char);
        steps.push({ action: 'create', message: `No child for '${char}' - creating new node`, node: char, char, isNew: true });
      } else {
        steps.push({ action: 'reuse', message: `Child for '${char}' exists - reusing node`, node: char, char, isNew: false });
      }
      node = child;
    }
    
    if (node.getChild('\0') === null) {
      node.addNewChild('\0');
      steps.push({ action: 'terminal', message: `Adding terminal node to complete "${str}"`, node: '\\0', char: '\0' });
    } else {
      steps.push({ action: 'exists', message: `"${str}" already exists in trie`, node: '\\0', char: '\0' });
    }
    
    return steps;
  }
  
  remove(str) {
    const steps = [];
    steps.push({ action: 'start', message: `Starting removal of "${str}"`, node: 'root' });
    this._removeRecursive(this.root, str, 0, steps);
    return steps;
  }
  
  _removeRecursive(node, str, idx, steps) {
    if (idx === str.length) {
      if (node.getChild('\0') !== null) {
        node.removeChild('\0');
        steps.push({ action: 'remove-terminal', message: `Removing terminal node for "${str}"`, char: '\\0' });
        return true;
      }
      steps.push({ action: 'not-found', message: `"${str}" not found in trie` });
      return false;
    }
    
    const char = str[idx];
    const child = node.getChild(char);
    if (child === null) {
      steps.push({ action: 'not-found', message: `No child for '${char}' - string not in trie` });
      return false;
    }
    
    const result = this._removeRecursive(child, str, idx + 1, steps);
    if (child.getChildCount() === 0) {
      node.removeChild(char);
      steps.push({ action: 'remove-node', message: `Removing node '${char}' (no remaining children)`, char });
    }
    return result;
  }
  
  getStrings() {
    const strings = [];
    this._collectStrings(this.root, '', strings);
    return strings;
  }
  
  _collectStrings(node, prefix, strings) {
    if (node.getChild('\0') !== null) {
      strings.push(prefix);
    }
    for (const [char, child] of node.children) {
      if (char !== '\0') {
        this._collectStrings(child, prefix + char, strings);
      }
    }
  }
  
  toTree() {
    return this._nodeToTree(this.root, 'root');
  }
  
  _nodeToTree(node, label) {
    const children = [];
    for (const [char, child] of node.children) {
      children.push(this._nodeToTree(child, char === '\0' ? '∅' : char));
    }
    return { label, children };
  }
}

const collectTreeElements = (node, depth = 0, x = 0, parentX = null, parentY = null, highlighted = [], lines = [], nodes = [], bounds = { minX: Infinity, maxX: -Infinity, maxY: 0 }) => {
  const nodeRadius = 18;
  const levelHeight = 60;
  const y = depth * levelHeight + 30;

  // Track bounds
  bounds.minX = Math.min(bounds.minX, x - nodeRadius);
  bounds.maxX = Math.max(bounds.maxX, x + nodeRadius);
  bounds.maxY = Math.max(bounds.maxY, y + nodeRadius);

  const isHighlighted = highlighted.includes(node.label);
  const isTerminal = node.label === '∅';

  if (parentX !== null) {
    lines.push({ x1: parentX, y1: parentY, x2: x, y2: y, key: `line-${depth}-${x}` });
  }

  nodes.push({
    x, y, nodeRadius, isTerminal, isHighlighted,
    label: node.label === 'root' ? '○' : node.label,
    key: `node-${depth}-${x}`
  });

  node.children.forEach((child, i) => {
    const spread = Math.max(50, 220 / (depth + 1));
    const childX = x + (i - (node.children.length - 1) / 2) * spread;
    collectTreeElements(child, depth + 1, childX, x, y, highlighted, lines, nodes, bounds);
  });

  return { lines, nodes, bounds };
};

const TreeVisualization = ({ treeData, highlighted }) => {
  const { lines, nodes, bounds } = collectTreeElements(treeData, 0, 300, null, null, highlighted, [], [], { minX: Infinity, maxX: -Infinity, maxY: 0 });

  // Calculate dynamic dimensions with padding
  const padding = 30;
  const width = Math.max(400, bounds.maxX - bounds.minX + padding * 2);
  const height = Math.max(150, bounds.maxY + padding);
  const viewBoxX = bounds.minX - padding;

  return (
    <svg width={width} height={height} viewBox={`${viewBoxX} 0 ${width} ${height}`}>
      {lines.map(line => (
        <line key={line.key} x1={line.x1} y1={line.y1} x2={line.x2} y2={line.y2} stroke="#64748b" strokeWidth={2} />
      ))}
      {nodes.map(node => (
        <g key={node.key}>
          <circle
            cx={node.x}
            cy={node.y}
            r={node.nodeRadius}
            fill={node.isTerminal ? '#94a3b8' : node.isHighlighted ? '#fbbf24' : '#3b82f6'}
            stroke={node.isHighlighted ? '#f59e0b' : '#1e40af'}
            strokeWidth={2}
          />
          <text x={node.x} y={node.y + 5} textAnchor="middle" fill="white" fontSize={14} fontWeight="bold">
            {node.label}
          </text>
        </g>
      ))}
    </svg>
  );
};

const CodeBlock = ({ code, title }) => (
  <div className="bg-slate-900 rounded-lg overflow-hidden">
    <div className="bg-slate-800 px-4 py-2 text-slate-300 text-sm font-mono">{title}</div>
    <pre className="p-4 text-sm text-slate-100 overflow-x-auto">
      <code>{code}</code>
    </pre>
  </div>
);

const insertCode = `public void insert(String textString) {
   TrieNode node = root;
   
   for (int i = 0; i < textString.length(); i++) {
      char character = textString.charAt(i);
      TrieNode child = node.getChild(character);
      if (child == null) {
         child = node.addNewChild(character);
      }
      node = child;
   }
   
   if (node.getChild('\\0') == null) {
      node.addNewChild('\\0');
   }
}`;

const removeCode = `public boolean remove(String textString) {
   return removeRecursive(root, textString, 0);
}

protected boolean removeRecursive(TrieNode node, 
   String textString, int charIndex) {
   
   if (charIndex == textString.length()) {
      if (node.getChild('\\0') != null) {
         node.removeChild('\\0');
         return true;
      }
      return false;
   }
   
   char character = textString.charAt(charIndex);
   TrieNode child = node.getChild(character);
   if (child == null) {
      return false;
   }
   
   boolean result = removeRecursive(child, textString, 
      charIndex + 1);
   if (child.getChildCount() == 0) {
      node.removeChild(character);
   } 
   return result;
}`;

export default function TrieLearningPlatform() {
  const [trie] = useState(() => new Trie());
  const [input, setInput] = useState('');
  const [strings, setStrings] = useState([]);
  const [treeData, setTreeData] = useState(trie.toTree());
  const [steps, setSteps] = useState([]);
  const [currentStep, setCurrentStep] = useState(-1);
  const [mode, setMode] = useState('insert');
  const [tab, setTab] = useState('interactive');

  const updateTrie = useCallback(() => {
    setStrings(trie.getStrings());
    setTreeData(trie.toTree());
  }, [trie]);

  const handleInsert = () => {
    if (!input.trim()) return;
    const str = input.toUpperCase().trim();
    const newSteps = trie.insert(str);
    setSteps(newSteps);
    setCurrentStep(0);
    updateTrie();
    setInput('');
  };

  const handleRemove = () => {
    if (!input.trim()) return;
    const str = input.toUpperCase().trim();
    const newSteps = trie.remove(str);
    setSteps(newSteps);
    setCurrentStep(0);
    updateTrie();
    setInput('');
  };

  const handleQuickAdd = (str) => {
    const newSteps = trie.insert(str);
    setSteps(newSteps);
    setCurrentStep(0);
    updateTrie();
  };

  const handleQuickRemove = (str) => {
    const newSteps = trie.remove(str);
    setSteps(newSteps);
    setCurrentStep(0);
    updateTrie();
  };

  const nextStep = () => {
    if (currentStep < steps.length - 1) {
      setCurrentStep(currentStep + 1);
    }
  };

  const prevStep = () => {
    if (currentStep > 0) {
      setCurrentStep(currentStep - 1);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 to-slate-800 text-white p-6">
      <div className="max-w-6xl mx-auto">
        <h1 className="text-3xl font-bold text-center mb-2">Trie Data Structure</h1>
        <p className="text-slate-400 text-center mb-6">Interactive Learning Platform</p>
        
        <div className="flex gap-2 justify-center mb-6">
          <button
            onClick={() => setTab('interactive')}
            className={`px-4 py-2 rounded-lg transition ${tab === 'interactive' ? 'bg-blue-600' : 'bg-slate-700 hover:bg-slate-600'}`}
          >
            Interactive Demo
          </button>
          <button
            onClick={() => setTab('insert-code')}
            className={`px-4 py-2 rounded-lg transition ${tab === 'insert-code' ? 'bg-blue-600' : 'bg-slate-700 hover:bg-slate-600'}`}
          >
            Insert Code
          </button>
          <button
            onClick={() => setTab('remove-code')}
            className={`px-4 py-2 rounded-lg transition ${tab === 'remove-code' ? 'bg-blue-600' : 'bg-slate-700 hover:bg-slate-600'}`}
          >
            Remove Code
          </button>
          <button
            onClick={() => setTab('guide')}
            className={`px-4 py-2 rounded-lg transition ${tab === 'guide' ? 'bg-blue-600' : 'bg-slate-700 hover:bg-slate-600'}`}
          >
            Step-by-Step Guide
          </button>
        </div>

        {tab === 'interactive' && (
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="bg-slate-800 rounded-xl p-6">
              <h2 className="text-xl font-semibold mb-4">Controls</h2>
              
              <div className="flex gap-2 mb-4">
                <button
                  onClick={() => setMode('insert')}
                  className={`flex-1 py-2 rounded-lg transition ${mode === 'insert' ? 'bg-green-600' : 'bg-slate-700 hover:bg-slate-600'}`}
                >
                  Insert Mode
                </button>
                <button
                  onClick={() => setMode('remove')}
                  className={`flex-1 py-2 rounded-lg transition ${mode === 'remove' ? 'bg-red-600' : 'bg-slate-700 hover:bg-slate-600'}`}
                >
                  Remove Mode
                </button>
              </div>
              
              <div className="flex gap-2 mb-4">
                <input
                  type="text"
                  value={input}
                  onChange={(e) => setInput(e.target.value.replace(/[^a-zA-Z]/g, ''))}
                  onKeyDown={(e) => e.key === 'Enter' && (mode === 'insert' ? handleInsert() : handleRemove())}
                  placeholder="Enter a word..."
                  className="flex-1 px-4 py-2 bg-slate-700 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  maxLength={10}
                />
                <button
                  onClick={mode === 'insert' ? handleInsert : handleRemove}
                  className={`px-6 py-2 rounded-lg transition ${mode === 'insert' ? 'bg-green-600 hover:bg-green-500' : 'bg-red-600 hover:bg-red-500'}`}
                >
                  {mode === 'insert' ? 'Insert' : 'Remove'}
                </button>
              </div>
              
              <div className="mb-4">
                <p className="text-sm text-slate-400 mb-2">Quick Add:</p>
                <div className="flex flex-wrap gap-2">
                  {['CAT', 'CATS', 'CAR', 'CARD', 'CARE'].map(w => (
                    <button
                      key={w}
                      onClick={() => handleQuickAdd(w)}
                      className="px-3 py-1 bg-slate-700 rounded hover:bg-green-600 text-sm transition"
                    >
                      {w}
                    </button>
                  ))}
                </div>
              </div>
              
              <div className="mb-4">
                <p className="text-sm text-slate-400 mb-2">Strings in Trie ({strings.length}):</p>
                <div className="flex flex-wrap gap-2 min-h-[40px]">
                  {strings.length === 0 ? (
                    <span className="text-slate-500 italic">Empty trie</span>
                  ) : (
                    strings.map(s => (
                      <span
                        key={s}
                        onClick={() => handleQuickRemove(s)}
                        className="px-3 py-1 bg-blue-600 rounded cursor-pointer hover:bg-red-600 text-sm transition"
                        title="Click to remove"
                      >
                        {s}
                      </span>
                    ))
                  )}
                </div>
              </div>
              
              {steps.length > 0 && (
                <div className="bg-slate-700 rounded-lg p-4">
                  <div className="flex justify-between items-center mb-2">
                    <span className="text-sm text-slate-400">Step {currentStep + 1} of {steps.length}</span>
                    <div className="flex gap-2">
                      <button onClick={prevStep} disabled={currentStep === 0} className="px-3 py-1 bg-slate-600 rounded disabled:opacity-50">←</button>
                      <button onClick={nextStep} disabled={currentStep === steps.length - 1} className="px-3 py-1 bg-slate-600 rounded disabled:opacity-50">→</button>
                    </div>
                  </div>
                  <p className="text-sm">{steps[currentStep]?.message}</p>
                </div>
              )}
            </div>
            
            <div className="bg-slate-800 rounded-xl p-6">
              <h2 className="text-xl font-semibold mb-4">Trie Visualization</h2>
              <div className="bg-slate-900 rounded-lg min-h-[200px] max-h-[500px] overflow-auto">
                <TreeVisualization treeData={treeData} highlighted={steps[currentStep]?.char ? [steps[currentStep].char === '\\0' ? '∅' : steps[currentStep].char] : []} />
              </div>
              <p className="text-xs text-slate-500 mt-2 text-center">○ = Root node, ∅ = Terminal node (end of string)</p>
            </div>
          </div>
        )}

        {tab === 'insert-code' && (
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <CodeBlock code={insertCode} title="Trie.java - insert()" />
            <div className="bg-slate-800 rounded-xl p-6">
              <h2 className="text-xl font-semibold mb-4">Insert Algorithm Explained</h2>
              <ol className="space-y-4 text-slate-300">
                <li className="flex gap-3">
                  <span className="bg-blue-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">1</span>
                  <span>Start at the <strong>root node</strong> and initialize the current node pointer.</span>
                </li>
                <li className="flex gap-3">
                  <span className="bg-blue-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">2</span>
                  <span>For each character in the string, check if the current node has a child for that character.</span>
                </li>
                <li className="flex gap-3">
                  <span className="bg-blue-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">3</span>
                  <span>If <strong>no child exists</strong>, create a new node and add it as a child.</span>
                </li>
                <li className="flex gap-3">
                  <span className="bg-blue-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">4</span>
                  <span>If a <strong>child exists</strong>, reuse it (don't create duplicates).</span>
                </li>
                <li className="flex gap-3">
                  <span className="bg-blue-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">5</span>
                  <span>Move to the child node and repeat for the next character.</span>
                </li>
                <li className="flex gap-3">
                  <span className="bg-blue-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">6</span>
                  <span>After all characters, add a <strong>terminal node</strong> ('\0') to mark the end of the string.</span>
                </li>
              </ol>
              <div className="mt-6 p-4 bg-slate-700 rounded-lg">
                <p className="text-sm text-slate-400">Time Complexity: <span className="text-green-400 font-mono">O(M)</span> where M is the string length</p>
              </div>
            </div>
          </div>
        )}

        {tab === 'remove-code' && (
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <CodeBlock code={removeCode} title="Trie.java - remove()" />
            <div className="bg-slate-800 rounded-xl p-6">
              <h2 className="text-xl font-semibold mb-4">Remove Algorithm Explained</h2>
              <ol className="space-y-4 text-slate-300">
                <li className="flex gap-3">
                  <span className="bg-red-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">1</span>
                  <span>Use <strong>recursion</strong> to traverse down to the terminal node of the string.</span>
                </li>
                <li className="flex gap-3">
                  <span className="bg-red-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">2</span>
                  <span>If the string doesn't exist (missing child), return <strong>false</strong>.</span>
                </li>
                <li className="flex gap-3">
                  <span className="bg-red-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">3</span>
                  <span>When you reach the end (charIndex == string length), remove the <strong>terminal node</strong>.</span>
                </li>
                <li className="flex gap-3">
                  <span className="bg-red-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">4</span>
                  <span>As recursion unwinds, check each node's <strong>child count</strong>.</span>
                </li>
                <li className="flex gap-3">
                  <span className="bg-red-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">5</span>
                  <span>If a node has <strong>no children</strong>, remove it (it's no longer needed).</span>
                </li>
                <li className="flex gap-3">
                  <span className="bg-red-600 w-6 h-6 rounded-full flex items-center justify-center text-sm flex-shrink-0">6</span>
                  <span>Nodes shared by other strings are <strong>preserved</strong>.</span>
                </li>
              </ol>
              <div className="mt-6 p-4 bg-slate-700 rounded-lg">
                <p className="text-sm text-slate-400">Time Complexity: <span className="text-green-400 font-mono">O(M)</span> where M is the string length</p>
              </div>
            </div>
          </div>
        )}

        {tab === 'guide' && (
          <div className="bg-slate-800 rounded-xl p-6">
            <h2 className="text-2xl font-semibold mb-6">Complete Step-by-Step Guide</h2>
            
            <div className="space-y-8">
              <section>
                <h3 className="text-xl font-semibold text-blue-400 mb-3">What is a Trie?</h3>
                <p className="text-slate-300 mb-3">A trie (pronounced "try") is a tree data structure for storing strings where each non-root node represents a single character. Nodes can be shared between strings with common prefixes, making tries highly efficient for prefix-based operations.</p>
                <div className="bg-slate-700 p-4 rounded-lg">
                  <p className="text-sm text-slate-400">Key Properties:</p>
                  <ul className="mt-2 space-y-1 text-slate-300">
                    <li>• Each node has at most one child per alphabet character</li>
                    <li>• Terminal nodes (∅) mark the end of strings</li>
                    <li>• Common prefixes share nodes (e.g., CAT and CATS share C, A, T)</li>
                  </ul>
                </div>
              </section>
              
              <section>
                <h3 className="text-xl font-semibold text-green-400 mb-3">Inserting "CAT" then "CATS"</h3>
                <div className="grid md:grid-cols-2 gap-4">
                  <div className="bg-slate-700 p-4 rounded-lg">
                    <p className="font-semibold mb-2">Insert "CAT":</p>
                    <ol className="text-sm text-slate-300 space-y-1">
                      <li>1. Start at root → no 'C' child → create 'C'</li>
                      <li>2. At 'C' → no 'A' child → create 'A'</li>
                      <li>3. At 'A' → no 'T' child → create 'T'</li>
                      <li>4. At 'T' → add terminal node ∅</li>
                      <li className="text-green-400">→ 4 new nodes created</li>
                    </ol>
                  </div>
                  <div className="bg-slate-700 p-4 rounded-lg">
                    <p className="font-semibold mb-2">Insert "CATS":</p>
                    <ol className="text-sm text-slate-300 space-y-1">
                      <li>1. Start at root → 'C' exists → reuse</li>
                      <li>2. At 'C' → 'A' exists → reuse</li>
                      <li>3. At 'A' → 'T' exists → reuse</li>
                      <li>4. At 'T' → no 'S' child → create 'S'</li>
                      <li>5. At 'S' → add terminal node ∅</li>
                      <li className="text-green-400">→ Only 2 new nodes created!</li>
                    </ol>
                  </div>
                </div>
              </section>
              
              <section>
                <h3 className="text-xl font-semibold text-red-400 mb-3">Removing "CAT" (while "CATS" remains)</h3>
                <div className="bg-slate-700 p-4 rounded-lg">
                  <ol className="text-slate-300 space-y-2">
                    <li><strong>1.</strong> Recursively traverse: root → C → A → T</li>
                    <li><strong>2.</strong> At 'T': Remove terminal node for "CAT"</li>
                    <li><strong>3.</strong> Check 'T': Still has child 'S' (for "CATS") → <span className="text-yellow-400">keep 'T'</span></li>
                    <li><strong>4.</strong> Check 'A': Still has child 'T' → <span className="text-yellow-400">keep 'A'</span></li>
                    <li><strong>5.</strong> Check 'C': Still has child 'A' → <span className="text-yellow-400">keep 'C'</span></li>
                    <li className="text-red-400 font-semibold">→ Only the terminal node removed! "CATS" is preserved.</li>
                  </ol>
                </div>
              </section>
              
              <section>
                <h3 className="text-xl font-semibold text-purple-400 mb-3">Try It Yourself!</h3>
                <p className="text-slate-300">Go to the <strong>Interactive Demo</strong> tab and try these exercises:</p>
                <ol className="mt-3 text-slate-300 space-y-2">
                  <li>1. Insert "CAT", "CAR", "CARD" and observe shared nodes</li>
                  <li>2. Remove "CAR" and see which nodes remain</li>
                  <li>3. Try inserting a string that already exists</li>
                  <li>4. Remove all strings and watch the trie clean up</li>
                </ol>
              </section>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
