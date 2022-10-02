package edu.ktu.screenshotanalyser.context;

public class TextRecognitionTests {

	/*
	
	@Test
	public void testCheckGrammar() throws IOException {
		JLanguageTool langTool = new JLanguageTool(new BritishEnglish());
		// comment in to use statistical ngram data:
		//langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
		List<RuleMatch> matches = langTool.check("A sentence with a error in the Hitchhiker's Guide tot he Galaxy");
		for (RuleMatch match : matches) {
		  System.out.println("Potential error at characters " +
		      match.getFromPos() + "-" + match.getToPos() + ": " +
		      match.getMessage());
		  System.out.println("Suggested correction(s): " +
		      match.getSuggestedReplacements());
		}
	}
	@Test
	public void testFindNouns() throws IOException, JWNLException {
		List<String> nouns = new ArrayList();
		String sentence = "A sentence with a error in the Hitchhiker's Guide to the Galaxy. Mistake exists.";
		Document doc = new Document(sentence);
		for (Sentence s : doc.sentences()) {
			for (int i = 0; i < s.words().size(); i++) {
				String tag = s.posTag(i);
				String word = s.word(i).toLowerCase();
				if (tag == null || !tag.contains("NN") || nouns.contains(word)) {
					continue;
				}
				nouns.add(word);
			}
		}
		Dictionary dictionary = Dictionary.getDefaultResourceInstance();
		List<String> syns = new ArrayList<String>();
		for (String w : nouns) {
			IndexWord indexWord = dictionary.getIndexWord(POS.NOUN, w);
			for (Synset x : indexWord.getSenses()) {
				for (Word xW : x.getWords()) {
					String lemma = xW.getLemma().toLowerCase();
					if (w.equals(lemma)) {
						continue;
					}
					if (nouns.contains(lemma)) {
						System.out.println(w+" "+xW.getLemma());	
					}
					
				}
			}
		}
		
	}
	@Test
	public void testExtractNouns() {
		Properties props = new Properties();
		// "tokenize,ssplit,pos,lemma,ner,parse,dcoref"
		props.put("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref");
		edu.stanford.nlp.pipeline.StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document = new Annotation("I am going home. Tom is not. Books are great.");
		pipeline.annotate(document);

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		String[] sentenceList = new String[sentences.size()];

		for (int i = 0; i < sentenceList.length; i++) {
			CoreMap sentence = sentences.get(i);
			System.out.println(sentence.toString());
			sentence.get(TokensAnnotation.class).stream().forEach((tok) -> {
				String PosTagg = tok.get(PartOfSpeechAnnotation.class);
				System.out.println(tok.category());

				System.out.println(PosTagg + " " + tok.originalText());
			});
			sentenceList[i] = sentence.toString();
		}

	}

	@Test
	public void testGetSynonyms() throws JWNLException, CloneNotSupportedException {
		Dictionary dictionary = Dictionary.getDefaultResourceInstance();
		IndexWord ACCOMPLISH = dictionary.getIndexWord(POS.VERB, "accomplish");
		IndexWord ACCOMPLISH2 = dictionary.getIndexWord(POS.VERB, "execute");
		RelationshipList list = RelationshipFinder.findRelationships(ACCOMPLISH.getSenses().get(0),
				ACCOMPLISH2.getSenses().get(0), PointerType.VERB_GROUP);

		for (Synset s : ACCOMPLISH.getSenses()) {
			System.out.println(s);

			for (Word w : s.getWords()) {
				System.out.println(w.getLemma());
			}
		}

	}


*/
}
